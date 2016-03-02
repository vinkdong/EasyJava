/**
 * Created by dongwenqi on 16-3-1.
 */
var easyjava = new Object({
    init: function () {
        var start = this.start();
        this.url = window.location.href;
        return start;
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
            type: 'get',
            url: self.url+'_rpc',
            data: field_list,
            async: true,
            success: function (msg) {
                alert('加载成功');
            },
            error: function (msg) {
                self.throw_err("<h3>警告</h3><p>网络请求 <strong>失败</strong>检查您的网络连接和设备环境</p><br/>");
            }
        });
    },

    throw_err : function(msg){
		alertify.alert(msg);
    },


});
easyjava.init();