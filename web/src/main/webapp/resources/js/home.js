$(document).ready(function() {
	try {
		$('*[data-tooltip="yes"]').tooltip();
	} catch (e) {
		console.log(e);
	}

	var fullPage = $('#fullpage');
	if (fullPage.length) {
		fullPage.fullpage({
			menu : '.side-navigation',
			lockAnchors : false,
			anchors : [ 'main', 'customer', 'developer' ]
		});
	}

	try {
		$('ul.side-navigation > li').click(function() {
			var arr = $(this).find('> a');
			$(arr).each(function() {
				location = $(this).attr('href');
			});
		});
	} catch (e) {
		console.log(e);
	}

	try {
		$('div.alert-message a.close').click(function() {
			$(this).parent().slideUp();
		});
	} catch (e) {
		console.log(e);
	}

});

function getParameterURL() {
	var url = location.pathname.substring(0, location.pathname.indexOf('/', 1));
	return location.origin + url;
}