/**
 * Created by dongwenqi on 16-3-1.
 */
var genericJsonRpc = function(params, fct) {
    var data = {
        jsonrpc: "2.0",
        params: params,
        id: Math.floor(Math.random() * 1000 * 1000 * 1000)
    };
    var xhr = fct(data);
    var result = xhr.pipe(function(result) {
        if (result.error !== undefined) {
            return $.Deferred().reject("server", result.error);
        } else {
            return result;
        }
    }, function() {
        var def = $.Deferred();
        return def.reject.apply(def, ["communication"].concat(_.toArray(arguments)));
    });
    result.abort = function () { xhr.abort && xhr.abort(); };
    return result;
};

var easyjava = new Object({
    init: function () {
        var start = this.start();
        this.res = {};
        this.url = this.getUrl();
        this.$el= "";
        return start;
    },
    getUrl: function() {
    	var self  = this;
    	var url = window.location.href;
    	res = {};
    	if (url.indexOf("#", 0)>0){
    		var parameters=url.substring(url.indexOf("#", 1)+1,url.length).split("&");
    		url=  url.substring(0, url.indexOf("#", 1));
    		
    		_.each(parameters,function(para){
    			res[para.substring(0,para.indexOf("=", 1))] = para.substring(para.indexOf("=", 1)+1,para.length);
    		});
    	}
    	if (url.indexOf("?", 0)>0){
    		url=  url.substring(0, url.indexOf("?", 1));
    	}
    	if(!jQuery.isEmptyObject(res)){
    		self.res = res ;
    		self.loadview(res,url).done(function(data){
			var s = $('.container.main');
			s.html(data);
			if(res.type=='tree'){
				$('.e_back').attr('style','display:none');
				$('.e_edit').attr('style','display:none');
			}
		});
    	}
    	return url;
	},
    start: function () {
        var self = this;

        $('.e_panel_submit').live('click', function () {
            event.preventDefault();
            var cr = $(this);
            //this.url = cr.context.baseURI;
            var id  = null;
            while(!id){
                cr = cr.parent();
                id = cr.find('.e_form').attr("data-id");
            }
            return self.add(self.read_field(cr,id)).done(function(data){
            	if(/^[0-9]+$/.test(data)){
            		self.read_rpc(res.model, data, "form").done(function(view){
            			var s = $('.container.main');
            			s.html(view);
            			$('.e_edit').attr('style','display:display');
            		}).fail(function(){
            			self.throw_err("<h3>警告</h3><p>网络请求 <strong>失败</strong>检查您的网络连接和设备环境</p><br/>");
            		});
            	}
            });
        });
        $('.e_panel_cancel').live('click', function (e) {
            e.preventDefault();
        	var id = $(this).parents().find('.e_form').attr("data-id");
        	self.res.type = 'view';
        	self.res.id = id;
        	if($(this).find('button').length>0){
        		return null;
        	}
        	return self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
    			$('.e_edit').attr('style','display:display');
        	});
        });
        $('.row.e_form').find("tbody tr td").live('click',function(e){
        	e.preventDefault();
        	var id = $(this).parent().attr("data-id");
        	self.res.type = 'view';
        	self.res.id = id;
        	if($(this).find('button').length>0){
        		return null;
        	}
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
    			$('.e_edit').attr('style','display:display');
        	})});
        $('.e_create').live('click',function(){
        	delete self.res.id;
        	self.res.type='view';
        	var cr = this;
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
            	$(cr).attr('style','display:none');
        	});
        });
        $('.e_edit').live('click',function(){
        	self.res.type='edit';
        	var cr = this;
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
            	$(cr).attr('style','display:none');
        	});
        });
        $('.e_back').live('click',function(){
        	self.res.type='tree';
        	var cr = this;
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_create').attr('style','display:display');
    			$('.e_edit').attr('style','display:none');
    			$(cr).attr('style','display:none');
        	});  	
        });
        $('.e_o2m_edit').live('click',function(){
        	res = {};
        	self.$el = $(this);
        	res.id = self.$el.parent().attr("data-id");
        	res.field = self.$el.parent().parent().attr('field');
        	res.model=self.res.model;
        	res.type = 'edit';
        	self.$el.attr('style','display:none');
        	self.$el.next().removeAttr('style');
        	self.loadO2mOp(res).done(function(e){
        		self.$el.parent().prev().html(e);
        	});
        });
        $('.e_o2m_submit').live('click',function(){
        	self.$el = $(this);
        	var id = self.$el.parent().attr("data-id");
        	res = self.read_field(self.$el.parent().prev(),id);
        	res.model=self.$el.parent().attr("model");
        	res[self.res.model+"_id"] = self.res.id;
        	res.type = 'commit';
        	self.$el.attr('style','display:none');
        	self.$el.prev().removeAttr('style');
        	return self.loadO2mOp(res).done(function(e){
        		e = Number(e);
        		if(e>0){
        			self.$el.parent().attr("data-id",e);
        			res.id = e;
        			res.field = self.$el.parent().parent().attr('field');
        			res.model = self.res.model;
        			res.type = 'view';
        			self.$el.parent().prev().attr("data-id",e);
        			self.loadO2mOp(res).done(function(e){
        				self.$el.parent().prev().html(e);
        			});
        		}
        	});
        });
        $('.e_o2m_delete').live('click',function(){
        	res = {};
        	self.$el = $(this);
        	res.id = self.$el.parent().attr("data-id");
        	res.model=self.$el.parent().attr("model");
        	res.type = "delete";
        	self.$el.parent().prev().remove();
        	self.$el.parent().remove();
        	return self.loadO2mOp(res);
        });
        $('.e_o2m_add').live('click',function(){
        	res = {};
        	self.$el = $(this);
        	res.id = self.$el.parent().attr("data-id");
        	res.field = self.$el.parent().attr('field');
        	res.model=self.res.model;
        	res.type = 'add';
        	self.$el.attr('style','display:none');
        	self.$el.next().removeAttr('style');
        	self.loadO2mOp(res).done(function(e){
        		self.$el.parent().append("<br/>"+e);
        		self.$el.parent().append("<a class=\"col-sm-10 e_o2m_add\">添加一个项目</a>");
        		self.$el.remove();
        	});
        });
        
        /**
         * 下拉框
         */
        $(".select p").live('click',function(e){
			$(".select").toggleClass('open');
			e.stopPropagation();
		});
		
		$(".e_m2o .select ul li").live('click',function(e){
			var _this=$(this);
			$(".select > p").text(_this.text());
			$(".select > p").attr('data-id',_this.attr('data-id'));
			_this.addClass("Selected").siblings().removeClass("Selected");
			$(".select").removeClass("open");
			e.stopPropagation();
		});
		
		$(document).on('click',function(){
			$(".select").removeClass("open");
		})

    },
    read_field : function(cr,id){
        var res = {id:id}
        _.each(cr.find('input'),function(node){
            res[node.getAttribute('name')] = $(node).val();
        });
        _.each(cr.find('textarea'),function(node){
            res[node.getAttribute('name')] = $(node).val();
        });
        _.each(cr.find('.e_m2o > div > p'),function(node){
        	res[node.getAttribute('name')] = node.getAttribute('data-id');
        });
        return res;
    },
    add: function(field_list){
        return this.add_rpc(field_list);
    },
    
    loadview:function(res,url){
    	 var self = this;
    	 window.location.hash ="model="+res.model+"&id="+res.id+"&type="+res.type;
         return genericJsonRpc(res, function (data) {
             return $.ajax(url+'_rpc_loadview', _.extend({}, '', {
                 url: url+'_rpc_loadview',
//                 dataType: 'json',
                 type: 'POST',
                 data: JSON.stringify(data, ''),
                 contentType: 'application/json'
             }));
         });
    },
    loadO2mOp:function(res){
   	 var self = this;
     return genericJsonRpc(res, function (data) {
            return $.ajax(self.url+'_rpc_o2m', _.extend({}, '', {
                url: self.url+'_rpc_o2m',
                type: 'POST',
                data: JSON.stringify(data, ''),
                contentType: 'application/json'
            }));
        });
    },   
    add_rpc: function (field_list) {
        var self = this;
        field_list.model =  self.res.model;
        return genericJsonRpc(field_list, function (data) {
            return $.ajax(self.url+'_rpc_add', _.extend({}, '', {
                url: self.url+'_rpc_add',
                dataType: 'json',
                type: 'POST',
                data: JSON.stringify(data, ''),
                contentType: 'application/json'
            }));
        });
    },
    
    read_rpc: function (model,id,operator) {
        var self = this;
		if(operator=='form'){
			window.location.hash ="model="+model+"&id="+id+"&type="+operator;
			res = {model:model,id:id,type:"form"};
			return self.loadview(res, self.url);
			$.ajax({
				type:'get',
				url:self.url+'_rpc_read',
				data:{model:model,id:id},
				async:true,
				success:function(msg){
					$('.e_form').html(msg);
					$('.e_button_edit').html("");
				},
	            error: function () {
	                self.throw_err("<h3>警告</h3><p>网络请求 <strong>失败</strong>检查您的网络连接和设备环境</p><br/>");
	            }
			});
		}
	},
	
    throw_err : function(msg){
		alertify.alert(msg);
    },
});
easyjava.init();
