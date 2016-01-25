var hourlySlider, fixedSlider;
var filterOpen = false;

$(document).ready(function() {
	$("#hourlyPayTypeSlider").slider({
		id : "hourlySlider",
		min : 0,
		max : 10,
		range : true
	}).slider('disable');
	$("#fixedPayTypeSlider").slider({
		id : "fixedSlider",
		min : 0,
		max : 10,
		range : true
	}).slider('disable');

	$('.checkbox > input[type="checkbox"]').change(function() {
		var parent = $(this).parent().parent();
		if (this.checked) {
			$(parent).find('input[id$="PayTypeSlider"]').slider("enable");
		} else {
			$(parent).find('input[id$="PayTypeSlider"]').slider("disable");
		}
	});

	$("#timeZoneSelect").select2();

	$('#filterHeader').click(function() {
		if (filterOpen) {
			$('#order_filter').slideUp();
			filterOpen = false;
		} else {
			$('#order_filter').slideDown();
			filterOpen = true;
		}
	});

	$("#hourlySortInput").bootstrapSwitch();
	$("#dateSortInput").bootstrapSwitch();

	$('#filterBtn').click(filterOrders);

	filterOrders();
});

function filterOrders() {
	var content = {};
	content.title = $('#order_title').val();
	var pagination = {};
	pagination.start = 0;
	pagination.step = 5;

	$.ajax({
		type : "POST",
		data : {
			content : JSON.stringify(content),
			page : JSON.stringify(pagination)
		},
		dataType : "application/json",
		url : getParameterURL() + "/orders/filter",
		error : function(e) {
			var response = JSON.parse(e.responseText);
			fillPagination(response.pages);
			fillOrders(response.items);
		},
		success : function(e) {
			fillPagination(e);
		}
	});
}

function fillOrders(data) {
	var items = $('#ordersArea');
	$(items).html('');
	$(data)
			.each(
					function() {
						var div = '	<div class="panel panel-info"><div class="panel-heading"><div class="panel-title">';
						div += this.title;
						div += '<div class="pull-right col-xs-3 col-sm-4"><div class="hidden-xs label label-warning col-sm-7">Added</div><div class="label label-info col-sm-5 pull-right">';
						div += this.payType;
						div += '</div></div></div></div><div class="panel-body"><div class="row"><div class="col-xs-10 col-sm-10">';
						div += this.descr;
						div += '/div><div class="pull-right col-xs-3 col-sm-4"><div class="hidden-xs label label-warning col-sm-7">';
						div += new Date(this.date);
						div += '/div><div class="label label-info col-sm-5 pull-right">';
						div += '</div></div></div></div><div class="panel-footer"><div class="row"><ul class="tags animated zoomIn"><li><a href="#" class="hvr-grow">Java</a></li><li><a href="#" class="hvr-grow">C#</a></li></ul></div></div></div>';
						$(items).append(div);
					});
}

function fillPagination(data) {
	var pagination = $('div[data-pagination="yes"]');
	$(pagination).html('');
	$(data).each(
			function() {
				if (this.first == 'current') {
					$(pagination).append(
							'<span class="page dark active">'
									+ (this.second + 1) + '</span>');
				} else {
					$(pagination).append(
							'<button data-page="first"="' + this.first
									+ '" class="page dark gradient">'
									+ (this.second + 1) + '</button>');
				}
			});
}