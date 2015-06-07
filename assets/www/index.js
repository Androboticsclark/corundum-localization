//デバッグ用
printf = function(str){
	window.alert(str);
//	document.writeln(str);
};


//コランダムホームが表示された（window.onloadから呼ばれる）
startCorundum = function(){
	//イメージピッカー起動
	var btn1 = document.getElementById("btn1");
	btn1.addEventListener("click", function(){
		var ival;
		var radioList = document.getElementsByName("isel");
		for(var i=0; i<radioList.length; i++){
			if(radioList[i].checked){
				ival = radioList[i].value;
			}
		}
		//引数をJSONで作成
		var jsVal = {
			"value":ival,				//camera or photoLibrary or photoAlbum
			"width":"300",
			"height":"200",
			"allowsEditing":"YES",		//YES or NO
			"saveAlbum":"editedImage"	//originalImage or editedImage or bothImage or NO
		}

		document.location = "corundum-api://imagePicker(" + JSON.stringify(jsVal) + ")";
	}, false);


	//シェィクイベント起動
	var btn2 = document.getElementById("btn2");
	btn2.addEventListener("click", function(){
		if(btn2.checked){
			document.location = "corundum-api://motionEvent({\"value\":\"shake\"})";
		}
		else {
			document.location = "corundum-api://motionEvent({\"value\":\"unShake\"})";
		}
	}, false);


	// URLスキーム実行
	var btn3 = document.getElementById("btn3");
	btn3.addEventListener("click", function(){
		document.location = "corundum-api://urlScheme({\"value\":\"" + txt1.value + "\"})";
	}, false);


	//ローカル通知起動
	var btn4 = document.getElementById("btn4");
	btn4.addEventListener("click", function(){
		var txt2 = document.getElementById("txt2");
		var txt3 = document.getElementById("txt3");

		var nsVal = {
			"value":txt3.value,			//Message
			"time":txt2.value			//Seconds
		};
		document.location = encodeURI("corundum-api://localNotice(" + JSON.stringify(nsVal) + ")");
	}, false);

	//自動スリープ起動
	var btn5 = document.getElementById("btn5");
	btn5.addEventListener("click", function(){
		if(btn4.checked){
			document.location = "corundum-api://autoSleep({\"value\":\"noSleep\"})";
		}
		else {
			document.location = "corundum-api://autoSleep({\"value\":\"sleep\"})";
		}
	}, false);

	//アドレスボタンがクリックされた
	var btn6 = document.getElementById("btn6");
	btn6.addEventListener("click", function(){
		document.location = "corundum-api://addressBook({\"value\":\"name\"})";
	}, false);

	//オペレーションモードボタンがクリックされた
	var btn7 = document.getElementById("btn7");
	btn7.addEventListener("click", function(){
		document.location = "corundum-api://operationMode({\"value\":\"true\"})";
	}, false);

	//フルスクリーンモードボタンがクリックされた
	var btn8 = document.getElementById("btn8");
	btn8.addEventListener("click", function(){
		document.location = "corundum-api://fullScreenMode({\"value\":\"true\"})";
	}, false);
};	//end of startCorundum


//コランダムホーム(JQueryMobile版が表示された（pagecreateから呼ばれる）
startJQMCorundum = function(){

	//イメージピッカー起動ボタンがクリックされた
	$('#imgbtn').live('click', function(){
		var iVal = $("input[name='radio-choice']:checked").val();
		//引数をJSONで作成
		var jsVal = {
			"value":iVal,				//camera or photoLibrary or photoAlbum
			"width":"300",
			"height":"200",
			"allowsEditing":"YES",		//YES or NO
			"saveAlbum":"editedImage"	//originalImage or editedImage or bothImage or NO
		}
		document.location = "corundum-api://imagePicker(" + JSON.stringify(jsVal) + ")";
	});


	//シェイクイベント取得フリップが変更された
	$('#flip-a').live('change', function(){
		var aVal = $("#flip-a").val();
		if(aVal === "yes"){
			document.location = "corundum-api://motionEvent({\"value\":\"shake\"})";
		}
		else {
			document.location = "corundum-api://motionEvent({\"value\":\"unShake\"})";
		}

	});


	// URLスキーム実行
	$('#urlbtn').live('click', function(){
		var uVal = $("#urltext").val();
		document.location = "corundum-api://urlScheme({\"value\":\"" + uVal + "\"})";
	});


	//ローカル通知ボタンがクリックされた
	$('#lclbtn').live('click', function(){
		var sec = $("#sec").val();
		var msg = $("#msg").val();

		var nsVal = {
			"value":msg,		//Message
			//"date":locstr		//yymmddhhmnsc で指定する
			"time":sec			//Seconds
		};
		document.location = encodeURI("corundum-api://localNotice(" + JSON.stringify(nsVal) + ")");
	});

	//自動スリープ設定フリップが変更された
	$('#flip-b').live('change', function(){
		var bVal = $("#flip-b").val();
		if(bVal === "yes"){
			document.location = "corundum-api://autoSleep({\"value\":\"sleep\"})";
		}
		else {
			document.location = "corundum-api://autoSleep({\"value\":\"noSleep\"})";
		}
	});

	//アドレス取得ボタンがクリックされた
	$('#adrsbtn').live('click', function(){
		document.location = "corundum-api://addressBook({\"value\":\"name\"})";
	});

	//オペレーションモードボタンがクリックされた
	$('#opemodebtn').live('click', function(){
		document.location = "corundum-api://operationMode({\"value\":\"true\"})";
	});

	//フルスクリーンモードボタンがクリックされた
	$('#fullmodebtn').live('click', function(){
		document.location = "corundum-api://fullScreenMode({\"value\":\"true\"})";
	});
}	//end of startJQMCorundum


//コランダムクラスを定義
function Corundum() {

	//イメージピッカーが選択された
	this.imagePicker = function(value){
		var imagin = document.getElementById("myImage");
		var decValue = utf.URLdecode(value);
		imagin.setAttribute("src", decValue);
	};


	//端末がシェィクされた
	this.motionShaked = function(value){
		printf("端末がシェィクされました" + value);
	};

	//URLスキーム実行
	this.urlScheme = function(value){
		//何もしない
	};

	//ローカル通知
	this.localNotice = function(value){
		//何もしない
	};

	//自動スリープが設定された
	this.autoSleep = function(value){
		printf("自動スリープモードが設定されました" + value);
	};

	//電話帳が取得された
	this.addressBook = function(value){
		var decValue = utf.URLdecode(value);
		if(decValue == ""){
			printf("ユーザー操作によりキャンセルされました");
		} else {
			printf(decValue);
		}
	};

	//オペレーションモードに変更された
	this.operationMode = function(value){
		printf("オペレーションモードに変更されました");
	};

	//フルスクリーンモードに変更された
	this.fullScreenMode = function(value){
		printf("フルスクリーンモードに変更されました");
	};
};	//end of Class Corundum()
