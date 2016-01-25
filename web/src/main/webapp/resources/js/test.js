jQuery(document).ready(function($){
    $("#history_title_test").click(function() {

        var type = $("#history_title_test").attr("rel");
        var sort_by_test = function(a, b) {
            var result = a.getElementsByClassName("hist_test_name")[0].innerHTML.toLowerCase().localeCompare(b.getElementsByClassName("hist_test_name")[0].innerHTML.toLowerCase());
            return type=="+"?result:-result;
        }
        $("#history_title_test").attr("rel", type=="+"?"-":"+");
        var list = $("#history_list .history-item").get();
        list.sort(sort_by_test);
        console.log("list: " + list);
        for (var i = 0; i < list.length; i++) {
            console.log(list[i]);
            list[i].parentNode.appendChild(list[i]);
        }
    });

    $("#history_title_technology").click(function() {
        var type = $("#history_title_technology").attr("rel");
        var sort_by_test = function(a, b) {
            var result = a.getElementsByClassName("hist_tech_name")[0].innerHTML.toLowerCase().localeCompare(b.getElementsByClassName("hist_tech_name")[0].innerHTML.toLowerCase());
            return type=="+"?result:-result;
        }
        $("#history_title_technology").attr("rel", type=="+"?"-":"+");
        var list = $("#history_list .history-item").get();
        list.sort(sort_by_test);
        console.log("list: " + list);
        for (var i = 0; i < list.length; i++) {
            console.log(list[i]);
            list[i].parentNode.appendChild(list[i]);
        }
    });
})