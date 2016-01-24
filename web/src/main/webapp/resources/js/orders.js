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
});