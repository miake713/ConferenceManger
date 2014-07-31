  var currentPage;
	document.onmousedown = function(){
		var h = document.body.clientHeight;
		var w=document.body.clientWidth;
		var x=event.clientX;
		var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;
     
		if(x>w/2)
		{
			window.scrollTo(0,document.body.scrollTop + h);	
	
		}
		else
		{
			window.scrollTo(0,document.body.scrollTop - h);
		 
		}
		 
	}

	
	window.onscroll= window.onresize = window.onload = function (){
		
	 var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
	 var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;  
	 currentPage=Math.ceil(document.body.scrollTop/document.body.clientHeight)+1;
 	 window.getCurrentPage.setCurrentPage(currentPage);
	 window.getPageCount.setPageCount(pageCount);
}

    window.onload=function ()
	{	
	   var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;
       window.getPageCount.setPageCount(pageCount);
	}


 function pagedown(x)
   {
   
	    var h = document.body.clientHeight;
		var w=document.body.clientWidth;
		//var x=event.clientX;
		var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;
		if(x>w/2)
		{
			window.scrollTo(0,document.body.scrollTop + h);		
		}
		else
		{
			window.scrollTo(0,document.body.scrollTop - h);
			 var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;	
		}
		 
		var currentPage=Math.ceil(document.body.scrollTop/document.body.clientHeight)+1;
		window.getCurrentPage.setCurrentPage(currentPage);

   
   }

function gotoPage(count)
{
	var h = document.body.clientHeight;
	window.scrollTo(0,count*h);		

}



function setfontSize(fontsize)
{
   var obj=document.getElementsByTagName("span");

	 for(var i=0;i<obj.length;i++)
	 {
		var objVal=obj[i];
		objVal.style.fontSize=fontsize+"pt";

	 }

	 var pageCount=parseInt(document.body.scrollHeight/document.body.clientHeight)+1;
	 currentPage=Math.ceil(document.body.scrollTop/document.body.clientHeight)+1;
	 window.getCurrentPage.setCurrentPage(currentPage);
	 window.getPageCount.setPageCount(pageCount);
}

