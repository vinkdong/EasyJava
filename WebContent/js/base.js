/**
 * Created by dongwenqi on 16-3-1.
 */
var easyjava = new Object({
    init: function () {
        var start = this.start();
        this.url = this.getUrl();
        return start;
    },
    getUrl: function() {
    	var  url = window.location.href;
    	if (url.indexOf("#", 0)>0){
    		url=  url.substring(0, url.indexOf("#", 1));
    	}
    	if (url.indexOf("?", 0)>0){
    		url=  url.substring(0, url.indexOf("?", 1));
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
            return self.add(self.read_field(cr,id));
        });
    },
    read_field : function(cr,id){
        var res = {id:id}
        _.each(cr.find('input'),function(node){
            res[node.getAttribute('name')] = $(node).val();
        });
        _.each(cr.find('textarea'),function(node){
            res[node.getAttribute('name')] = $(node).val();
        });
        return res;
    },
    add: function(field_list){
        this.add_rpc(field_list);
    },

    add_rpc: function (field_list) {
        var self = this;
        $.ajax({
            type: 'post',
            url: self.url+'_rpc_add',
            data: field_list,
            async: true,
            success: function (msg) {
            	if(/^[0-9]+$/.test(msg)){
            		self.read_rpc("forum", msg, "view");
            	}
            },
            error: function (msg) {
                self.throw_err("<h3>警告</h3><p>网络请求 <strong>失败</strong>检查您的网络连接和设备环境</p><br/>");
            }
        });
    },
    
    read_rpc: function (model,id,operator) {
        var self = this;
		if(operator=='view'){
			window.location.hash ="model="+model+"&id="+id+"&view="+operator;
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