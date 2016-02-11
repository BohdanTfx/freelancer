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