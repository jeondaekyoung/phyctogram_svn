<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>	
  <jsp:include page="../include/head.jsp"/>
  <jsp:include page="../include/head-index.jsp"/>
  <script src="<%=application.getContextPath()%>/resources/res/js/typewriter.js"></script>
  <script>
	  $(function () {
	      var typewriter = new Typewriter($("#typewriter"));
	      typewriter.typeText("The Most Advanced Growth Measuring, Management Device For Kids");
	  });
  </script>
</head>

<body>
  <jsp:include page="../include/nav.jsp"/>
  
<div id="fullpage">

  <div class="section " id="section00">
    <video autoplay loop muted id="myVideo">
      <source src="<%=application.getContextPath()%>/resources/res/imgs/video-phyctogram.mp4" type="video/mp4">
    </video>
    <div class="modal">
      <div class="layer animated dur400 fadeInUp">
        <h1 id="typewriter"></h1>
        <p>Phyctogram measures height and weight easily and accurately, and provides growth solution by automatically analysis</p>
        <!-- <a class="linkProd" href="product.html">제품보기</a> -->
      </div>
    </div>
  </div>

	<div class="section row" id="section0">
    <div class="headlineProd animated">
      <h3>Easy to use & Simple UI</h3>
      <p>Phyctogram provides a simple method to measure height and its aesthetic design aims at simpleness.</p>
      <img src="<%=application.getContextPath()%>/resources/res/imgs/main_2.png" alt="phyctogram" class="animated fadeInUpBig col-16">
    </div>
	</div>
	<div class="section" id="section1">
		<div class="wrap row">
			<div class="imgsContainer col-7">
				<img src="<%=application.getContextPath()%>/resources/res/imgs/0.png" alt="main" id="iphone2" />
				<img src="<%=application.getContextPath()%>/resources/res/imgs/2.png" alt="growth card" id="iphone3" />
			</div>

			<div class="box box-1 col-5 animated">
				<h2>Smart growth management service by the application</h2>
				Children’s data can be linked up application via WI-FI and manage systematic.
			</div>
		</div>
	</div>

	<div class="section moveDown" id="section2">
		<div class="wrap">
			<div class="imgsContainer">
				<img src="<%=application.getContextPath()%>/resources/res/imgs/4-en.png" width="322" alt="report" id="iphone-red" />
				<img src="<%=application.getContextPath()%>/resources/res/imgs/3-en.png" width="322" alt="check lists" id="iphone-blue" />
			</div>

			<div class="box box-2 animated">
				<h2>Provide growth comparison solution and predicted data</h2>
				<ul>
		          <li>You can perform statistical analysis based on data all users input on Phyctogram and manage data of your kids specifically</li>
		        </ul>
			</div>
		</div>
	</div>

	<div class="section moveDown" id="section3">
		<div class="wrap">
			<div class="box box-3 animated">
				<h2>Offer contents associated growth</h2>
				You can creating contents about baby book and childcare  including the process of growth.
			</div>
		</div>
		<div class="imgsContainer">
			<img src="<%=application.getContextPath()%>/resources/res/imgs/11-en.png" alt="baby book" id="iphone-two" />
		</div>
	</div>
  </div><!-- 끝: #fullpage -->
  
  <jsp:include page="../include/footer.jsp"/>
</body>
</html>