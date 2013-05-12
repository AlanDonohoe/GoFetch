// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// A generic onclick callback function.
function genericOnClick(info, tab) {
  var target = encodeURIComponent(info.linkUrl);
  var source = encodeURIComponent(info.pageUrl);
  var anchor = encodeURIComponent(info.selectionText);
  chrome.tabs.create({'url': 'http://gofetchdata.appspot.com/enterlink.jsf?target='+target+'&source='+source+'&anchor='+anchor}, function(window) {
  	
  });
}


chrome.contextMenus.create({
	"title": "Save this link", 
	"contexts":['link'],
    "onclick": genericOnClick
});