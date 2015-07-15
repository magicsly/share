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
                    h[h.length] = "名称:"+ d.list[i].name+" ";
                    h[h.length] = "pid:<font id='pid'>"+ d.list[i].pid+"</font> ";
                    h[h.length] = '<input type="button" value="查看" />'+" ";
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
                    h[h.length] = "净值:"+ d.val;
                    for(var i=0;i< d.list.length;i++) {
                        if (d.list[i].sid == "money") {
                            h[h.length] = "<p id='money'>";
                            h[h.length] = "现金:" + (d.list[i].nowmuch * d.val)+ " ";
                            h[h.length] = "</p>";
                        } else {
                            h[h.length] = "<p id='"+d.list[i].piId+"'>";
                            h[h.length] = "id:" + d.list[i].piId + " ";
                            h[h.length] = "股票名称:<font id='sname'>" + d.list[i].sname + "</font> ";
                            h[h.length] = "所持数量:" + d.list[i].nowmuch + " ";
                            h[h.length] = "成本价格:" + d.list[i].costprice + " ";
                            var price = d.nowprice[i-1].split(",")[1];
                            var much = d.list[i].nowmuch;
                            var v = price*much/d.val*100;
                            h[h.length] = "当前价格:" +  price + " ";
                            h[h.length] = "所占百分比:" +  "<input type='text' value='"+v+"' id='bfb'>" + "% ";
                            //h[h.length] = '所持数量:<input type="text" value="'+d.list[i].sname+'" />';
                            h[h.length] = "</p>";
                        }
                    }
                    h[h.length] = '<input type="button" value="调整" id="edit"/>';
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