function getSavedStateData() {
	var data = localStorage.getItem("openTaskStateReloadedData");
	data = JSON.parse(data);
	if (data == null || data == undefined)
		return undefined;

	var currentUrl = window.location.href;

	if (data.location != currentUrl) {
		localStorage.removeItem("openTaskStateReloadedData");
		return undefined;
	}

	return data.content;
}

function getURLWithoutParameters() {
	var url = window.location.href;
	var hasParam = url.indexOf('?') > 0;

	return hasParam ? url.toString().substring(0, url.indexOf('?')) : url;
}

function transformToUrlQuery(obj) {
	var query = "";
	for ( var param in obj)
		if (obj.hasOwnProperty(param) && obj[param] !== undefined)
			query += param + '=' + obj[param] + '&';

	query = query.length > 2 ? query.substring(0, query.length - 1) : query;
	return query;
}

function isNotEmpty(obj) {
	for ( var prop in obj)
		if (obj.hasOwnProperty(prop) && obj[prop] !== undefined)
			return true;
	return false;
}

function handleForbidden(url, alertFn) {
	if (url === undefined)
		window.location = "/";
	else
		window.location = url;
	alertFn();
}

function getUrlVars() {
	var vars = [], hash;
	var hashes = window.location.href.slice(
			window.location.href.indexOf('?') + 1).split('&');
	for (var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}

function getTimeZones() {
	return [ {
		zone : "-12",
		title : "-12 Baker island, Howland island",
		ticked : false
	}, {
		zone : "-11",
		title : "-11 American Samoa, Niue",
		ticked : false
	}, {
		zone : "-10",
		title : "-10 Hawaii",
		ticked : false
	}, {
		zone : "-9",
		title : "-9 Marquesas Islands, Gamblie Islands",
		ticked : false
	}, {
		zone : "-8",
		title : "-8 British Columbia, Mexico, California",
		ticked : false
	}, {
		zone : "-7",
		title : "-7 British Columbia, US Arizona",
		ticked : false
	}, {
		zone : "-6",
		title : "-6 Canada Saskatchewan, Costa Rica, Guatemala, Honduras",
		ticked : false
	}, {
		zone : "-5",
		title : "-5 Colombia, Cuba, Ecuador, Peru",
		ticked : false
	}, {
		zone : "-4",
		title : "-4 Venezuela, Bolivia, Brazil,	Barbados",
		ticked : false
	}, {
		zone : "-3",
		title : "-3 Newfoundland, Argentina, Chile",
		ticked : false
	}, {
		zone : "-2",
		title : "-2 South Georgia",
		ticked : false
	}, {
		zone : "-1",
		title : "-1 Capa Verde",
		ticked : false
	}, {
		zone : "0",
		title : "0 Ghana, Iceland, Senegal",
		ticked : false
	}, {
		zone : "1",
		title : "+1 Algeria, Nigeria, Tunisia",
		ticked : false
	}, {
		zone : "2",
		title : "+2 Ukraine, Zambia, Egypt",
		ticked : false
	}, {
		zone : "3",
		title : "+3 Belarus, Iraq, Iran",
		ticked : false
	}, {
		zone : "4",
		title : "+4 Armenia, Georgia, Oman",
		ticked : false
	}, {
		zone : "5",
		title : "+5 Kazakhstan, Pakistan, India",
		ticked : false
	}, {
		zone : "6",
		title : "+6 Ural, Bangladesh",
		ticked : false
	}, {
		zone : "7",
		title : "+7 Western Indonesai, Thailand",
		ticked : false
	}, {
		zone : "8",
		title : "+8 Hong Kong, China, Taiwan, Australia",
		ticked : false
	}, {
		zone : "9",
		title : "+9 Timor,Japan",
		ticked : false
	}, {
		zone : "10",
		title : "+10 New Guinea, Australia",
		ticked : false
	}, {
		zone : "11",
		title : "+11 Solomon Islands, Vanuatu",
		ticked : false
	}, {
		zone : "12",
		title : "+12 New zealand, Kamchatka, Kiribati",
		ticked : false
	} ];
}

function getPermissions() {
	return {
		orders : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		developers : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		createOrder : {
			unknown : false,
			administrator : false,
			developer : false,
			customer : true
		},
		order : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		signup : {
			unknown : true,
			administrator : false,
			developer : false,
			customer : false
		},
		personal : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		tests : {
			unknown : false,
			administrator : false,
			developer : true,
			customer : false
		},
		myworks : {
			unknown : false,
			administrator : false,
			developer : true,
			customer : true
		},
		test : {
			unknown : false,
			administrator : false,
			developer : true,
			customer : false
		},
		home : {
			unknown : true,
			administrator : true,
			developer : true,
			customer : true
		},
		auth : {
			unknown : true,
			administrator : false,
			developer : false,
			customer : false
		},
		pubdev : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		custpub : {
			unknown : false,
			administrator : true,
			developer : true,
			customer : true
		},
		createtest : {
			unknown : false,
			administrator : true,
			developer : false,
			customer : false
		},
		statistics : {
			unknown : false,
			administrator : true,
			developer : false,
			customer : false
		},
		createAdmin : {
			unknown : false,
			administrator : true,
			developer : false,
			customer : false
		},
		signupadmin : {
			unknown : true,
			administrator : false,
			developer : false,
			customer : false
		},
		forgot : {
			unknown : true,
			administrator : false,
			developer : false,
			customer : false
		},
		banservice : {
			unknown : false,
			administrator : true,
			developer : false,
			customer : false
		},
		technologies : {
			unknown : false,
			administrator : true,
			developer : false,
			customer : false
		},
		presentation : {
			unknown : true,
			administrator : true,
			developer : true,
			customer : true
		}
	};
}