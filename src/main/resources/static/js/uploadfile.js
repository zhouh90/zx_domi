var AllowExt = ".jpg|.gif|.bmp|.png|";
/*ext：限制扩展名.jpg 或者 .jpg|.png */
function checkFile(dom,maxSize,w,h,ext,cb) {
	//var extMsg = '.JPG .BMP .GIF .PNG';
	if(ext == ''){
		ext = AllowExt;
	}else if(ext.substr(0,1) != '.'){
		ext = '.' + ext.toLowerCase();
	}
	var extMsg = ext.replace('|',' ');
	while(extMsg.indexOf('|') > 0){
		extMsg = extMsg.replace('|',' ');
	}
	extMsg = extMsg.toUpperCase()+' ';
	//input maxSize unit : KB
	var msg = '1MB';
	if(maxSize >= 1024){
		msg = maxSize / 1024 +'MB';
	}else{
		msg = maxSize + 'KB';
	}
	var limitSize = maxSize * 1024;
    var imgfile = $.trim(dom.val());
    var FileExt = imgfile.substr(imgfile.lastIndexOf(".")).toLowerCase(); // .jpg ...
    if ((dom[0].files[0].size > limitSize) || (imgfile == "" || ext.indexOf(FileExt) == -1)) {
        //JEND.page.alert('请使用 '+extMsg+'格式并且小于'+msg+'的图片文件!');
    	cb({'check':false,'msg':'请使用 '+extMsg+'格式并且小于'+msg+'的图片文件!'});
		return ;
    }
	getImageWH(dom,function (obj){
		if(w > 0 && h > 0 && (obj.width != w || obj.height != h)){
			//JEND.page.alert('请使用 '+extMsg+'格式,尺寸为' + w + 'x' + h + '像素并且小于'+msg+'的图片文件!');
			cb({'check':false,'msg':'请使用 '+extMsg+'格式,尺寸为' + w + 'x' + h + '像素并且小于'+msg+'的图片文件!'});
			return ;
	    }else{
	    	cb({'check':true,'msg':'图片文件校验通过'});
	    	return ;
	    }
	});
}

function getImageWH(dom,callback){
	var file = dom[0].files[0];
	var fileReader  = new FileReader();
	fileReader.onload = function(e){
		var imgData = e.target.result;//获取图片的文件流
		//通过Image 对象去加载图片
		var image = new Image();
		image.onload = function(){
			width = image.width;
			height = image.height;
			callback && callback({"width": width, "height": height});
		}
		image.src = imgData;
	}
	fileReader.readAsDataURL(file);
	
}

/* 上传图片 */
/*maxSize:图片最大尺寸（KB），w:图片限制宽度（<0 则不限制），h:图片限制高度（<0 则不限制） */
function uploadAvatar(domid,maxSize,w,h,ext) {
	var imgUploadUrl = "http://merchant.beta.ule.com/merchantservice/h5/uploadImgServlet?merchantId=11223344";
	var dom = $('#'+domid);
	checkFile(dom,maxSize,w,h,ext,function(checkRes){
		if(checkRes.check){
			var data = new FormData();
			$.each(dom[0].files, function(i, file) {
				data.append('image', file);
			});
			$.ajax({
				type : "POST",
				dataType : "json",
//				dataType: "jsonp",
//			    jsonp: "callback",
				url : imgUploadUrl,
				data : data,
				cache : false,
				contentType : false, // 不可缺
				processData : false, // 不可缺
				success : function(jsonResult) {
					if(jsonResult && jsonResult.returnCode==='0000'){
						alert("上传失败");
					}else{
						alert("上传失败");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("上传失败");
				}
			});
		}else{
			JEND.page.alert(checkRes.msg);
		}
	});
}