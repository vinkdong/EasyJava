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
        this.res = {};
        this.url = this.getUrl();
        var start = this.start();
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
        $(document).on("click",".e_panel_submit",function(event){
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
        $(document).on("click",".e_panel_cancel",function(e){
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
        $(document).on("click",".row.e_form tbody tr td",function(e){
        	e.preventDefault();
        	if($(this).find('button').length>0||$(this).find('.e_m2m_add').length>0){
        		return null;
        	}
        	var id = $(this).parent().attr("data-id");
        	self.res.type = 'view';
        	self.res.id = id;
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
    			$('.e_edit').attr('style','display:display');
        	})});
        $(document).on("click",".e_create",function(){
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
        $(document).on("click",".e_edit",function(){
        	self.res.type='edit';
        	var cr = this;
        	self.loadview(self.res,self.url).done(function(data){
    			var s = $('.container.main');
    			s.html(data);
    			$('.e_back').attr('style','display:display');
            	$(cr).attr('style','display:none');
        	});
        });
        $(document).on("click",".e_back",function(){
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
        $(document).on("click",".e_o2m_edit",function(){
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
        $(document).on("click",".e_o2m_submit",function(){
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
        $(document).on("click",".e_o2m_delete",function(){
        	res = {};
        	self.$el = $(this);
        	res.id = self.$el.parent().attr("data-id");
        	res.model=self.$el.parent().attr("model");
        	res.type = "delete";
        	self.$el.parent().prev().remove();
        	self.$el.parent().remove();
        	return self.loadO2mOp(res);
        });
        $(document).on("click",".select-all",function(){
        	self.$el = $(this).closest('table');
        	var lines = self.$el.find('tbody tr');
        	_.each(lines,function(line){
        		var checkbox = $(line).children(":first").children(":first");
        		if(checkbox.prop('checked')==true){
        			checkbox.prop('checked',false);
        		}
        		else{
        			checkbox.prop('checked',true);
        		}
        	});
        });
        $(document).on("click",".e_o2m_add",function(){
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
        $(document).on("click",".e_m2m_add",function(e){  
        	e.preventDefault();
        	self.$el = $(this);
        	res.model =self.res.model;
        	res.field = self.$el.parents().find('table').attr('field');
        	return self.loadM2mOp(res).done(function(e){
        		var module = 	'<div class="modal fade e_m2m_dialog" id="'+res.field+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">\n'+
								'    <div class="modal-dialog" role="document">\n'+
								'        <div class="modal-content">\n'+
								'           <div class="modal-header">\n'+
								'                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\n'+
								'                <h4 class="modal-title" id="myModalLabel">Modal title</h4>\n'+
								'            </div>\n'+
								'            <div class="modal-body">\n'+e+
								'            </div>\n'+
								'            <div class="modal-footer">\n'+
								'                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>\n'+
								'                <button type="button" class="btn btn-primary add-m2m">Save changes</button>\n'+
								'            </div>\n'+
								'        </div>\n'+
								'    </div>\n'+
								'</div>\n';
        		$('body').append(module);
        		$('#'+res.field+'').modal('show');
        	});
        	
        })  
//        $('.e_m2m_add').live('click',function(e){
//        	e.preventDefault();
//        	
//        });
        
        $(document).on("click",".add-m2m",function(e){  
			e.stopPropagation();
			self.$el = $(this).closest('.modal-content');
			var body  = self.$el.find('.modal-body tbody tr');
			var ids = [];
			_.each(body,function(line){
				var selector = $(line).children(":first").children(":first");
				if(selector.is(':checked')){
					ids.push(selector.parent().attr('data-id'));
				}
			});
			res.id = self.res.id;
			res.model = self.res.model;
			res.field = self.$el.closest('.e_m2m_dialog').attr('id');
			res.ids = ids;
			res.type = 'add';
			alert(self.res.id);
			self.loadM2mOp(res).done(function(e){
				
			});
		});
        
        /**
         * 下拉框
         */
        $(document).on("click",".select p",function(e){  
			$(".select").toggleClass('open');
			e.stopPropagation();
		});
		
        $(document).on("click",".e_m2o .select ul li",function(e){  
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
    loadM2mOp:function(res){
    	var self = this;
    	return genericJsonRpc(res, function (data) {
            return $.ajax(self.url+'_rpc_m2m', _.extend({}, '', {
                url: self.url+'_rpc_m2m',
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
