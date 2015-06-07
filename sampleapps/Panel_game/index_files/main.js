var intGame = 0;
var intPanel = new Array(16);
var intStack = new Array(16);
var intBlank = 15;

function changePanel(intID)
{
	var intX = getX(intID);
	var intY = getY(intID);
	if (intX == 0 && intY == 0) return false;
	changePanelNext(intX, 1);
	changePanelNext(intY, 4);
	intBlank = intID;
	return true;
}

function changePanelNext(intSize, intMove)
{
	if (intSize == 0) return false;
	var i = 0;
	var j = 0;
	var intAbs = getAbs(intSize);
	var intSgn = getSgn(intSize);
	var intID = intBlank;
	var intNext = 0;
	for (i = 0; i < intAbs; i++)
	{
		intNext = intID + intSgn * intMove;
		j = intPanel[intID];
		intPanel[intID] = intPanel[intNext];
		intPanel[intNext] = j;
		intID = intNext;
	}
	return true;
}

function clickPanel(intID)
{
	if (intGame != 1) return false;
	if (changePanel(intID)) if (showPanel())
	{
		intGame = 0;
		alert("ゲーム クリアです。 おめでとうございます！");
	}
	return true;
}

function getAbs(intValue)
{
	if (intValue >= 0) return intValue; else return -intValue;
}

function getSgn(intValue)
{
	if (intValue == 0) return 0;
	if (intValue > 0) return 1; else return -1;
}

function getX(intID)
{
	if (Math.floor(intID / 4) == Math.floor(intBlank / 4)) return intID % 4 - intBlank % 4; else return 0;
}

function getY(intID)
{
	if (intID % 4 == intBlank % 4) return Math.floor(intID / 4) - Math.floor(intBlank / 4); else return 0;
}

function showPanel()
{
	var i = 0;
	var j = 1;
	for (i = 0; i < 16; i++)
	{
		if (intPanel[i] != intStack[i])
		{
			document.getElementById("divPanel" + i).value = (intPanel[i] + 1) + "";
			if (intPanel[i] == 15) document.getElementById("divPanel" + i).style.visibility = "hidden"; else document.getElementById("divPanel" + i).style.visibility = "visible";
		}
		if (intPanel[i] != i) j = 0;
		intStack[i] = intPanel[i];
	}
	return j == 1;
}

function startGame()
{
	if (!blnLoadComplete)
	{
		alert("ページの読み込みが完了するまで、しばらくお待ちください。");
		return false;
	}
	if (intGame == 2) return false;
	var i = 0;
	if (intGame == 1) if (!confirm("現在のゲームを中断して、再スタートしますか？")) return true;
	intGame = 2;
	for (i = 0; i < 16; i++)
	{
		intPanel[i] = i;
		intStack[i] = -1;
	}
	intBlank = 15;
	for (i = 0; i < 200; i++) changePanel(Math.floor(Math.random() * 16) % 16);
	showPanel();
	intGame = 1;
	alert("ゲームをスタートします。");
}
