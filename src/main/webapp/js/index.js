/**
 * Created by ElNino on 15/7/13.
 */
$(function(){
    index.loadpro();
    index.loadInfo();

})
var index = {
    loadpro : function(){
        $.ajax({
            url:"/userproject_list?uid=4",
            type :"get",
            dataType : "json",
            success : function(d){
                var h = [];
               for(var i=0;i< d.list.length;i++){
                    h[h.length] = "<p>";
                    h[h.length] = "����:"+ d.list[i].name+" ";
                    h[h.length] = "pid:<font id='pid'>"+ d.list[i].pid+"</font> ";
                    h[h.length] = '<input type="button" value="�鿴" />'+" ";
                    h[h.length] = "</p>";
               }
               $("#pro").html(h.join(""));
            },
            error :function(){return false;}
        })
    },
    loadInfo : function(){
        $("#pro input").live("click",function(){
            var pid = $(this).parent().find("#pid").text();
            $.ajax({
                url:"projectinfo_list?pid="+pid,
                type :"get",
                dataType : "json",
                success : function(d){
                    var h=[];
                    h[h.length] = "��ֵ:"+ d.val;
                    for(var i=0;i< d.list.length;i++) {
                        if (d.list[i].sid == "money") {
                            h[h.length] = "<p id='money'>";
                            h[h.length] = "�ֽ�:" + (d.list[i].nowmuch * d.val)+ " ";
                            h[h.length] = "</p>";
                        } else {
                            h[h.length] = "<p id='"+d.list[i].piId+"'>";
                            h[h.length] = "id:" + d.list[i].piId + " ";
                            h[h.length] = "��Ʊ����:<font id='sname'>" + d.list[i].sname + "</font> ";
                            h[h.length] = "��������:" + d.list[i].nowmuch + " ";
                            h[h.length] = "�ɱ��۸�:" + d.list[i].costprice + " ";
                            var price = d.nowprice[i-1].split(",")[1];
                            var much = d.list[i].nowmuch;
                            var v = price*much/d.val*100;
                            h[h.length] = "��ǰ�۸�:" +  price + " ";
                            h[h.length] = "��ռ�ٷֱ�:" +  "<input type='text' value='"+v+"' id='bfb'>" + "% ";
                            //h[h.length] = '��������:<input type="text" value="'+d.list[i].sname+'" />';
                            h[h.length] = "</p>";
                        }
                    }
                    h[h.length] = '<input type="button" value="����" id="edit"/>';
                    $("#proinfo").html(h.join(""));
                    index.proedit();
                },
                error :function(){return false;}
            })
        });
    },
    proedit : function(){
        $("#edit").live("click",function(){
            var str = [];
            $("#proinfo p").each(function(i){
                var id = $(this).attr("id");
                var ss = [];
                if(id!="money"){
                    ss[ss.length] = id;
                    ss[ss.length] = $(this).find("#sname").text();
                    ss[ss.length] = $(this).find("#bfb").val()/100;
                    str[str.length]=ss.join("|");
                }
            });
            var jstr = str.join(";");
            var pid = $("#pid").text();
            var url = "/editproject?pid="+pid+"&str="+jstr;
            $.ajax({
                url:url,
                type :"get",
                dataType : "json",
                success : function(d){

                },
                error :function(){return false;}
            })

        });
    }
}