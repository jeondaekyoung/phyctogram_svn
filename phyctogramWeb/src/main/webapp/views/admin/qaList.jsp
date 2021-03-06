<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>문의 (App)</title>
<jsp:include page="include/head.jsp"/>
</head>

<body>
  	
  <!-- header -->
 	<jsp:include page="include/header.jsp" flush="false"/>
  <!-- / header -->

	<section id="content">
		<div class="bg-dark lt">
			<div class="container">
				<div class="m-b-lg m-t-lg">
					<h3 class="m-b-none">고객센터 (App)</h3>
					<small class="text-muted">Customer (App)</small>
				</div>
			</div>
		</div>
		<div>
			<div class="container m-t-xl">
				<div class="row">
					<div class="col-sm-12">
						<div class="panel-group m-b min-h" id="accordion2">
							<div class="panel panel-default">
								<header class="panel-heading">
									<div class="row text-sm wrapper">
										<div class="col-sm-12">
											<div class="input-group">
												<input type="checkbox" id="answer" value="answer" checked="checked"> 답변 대기만 보기
												<input type="checkbox" id="total" value="total"> 전체
												<span class="input-group-btn"><button class="btn btn-sm btn-danger" type="button" id="search">검색</button></span>
											</div>
										</div>
									</div>
								</header>
							</div>
						</div>
						<button class="btn btn-danger btn-block btn-lg m-b-sm" id="moreBtn">더보기</button>
					</div>
				</div>
			</div>
		</div>
	</section>
 
	<!-- footer -->
	  <jsp:include page="include/footer.jsp" flush="false"/>
    <!-- / footer --> 

	
	<script type="text/javascript">
		
		var pageCnt = 0;
		var pageEnd = false;
		var searchState = "answer";
		var answerState;
		
		$(document).ready(function() {
			//목록읽어오기
			qaList();
			
			//답변 대기만 보기 체크
			  $("#answer").change(function(){
				  if(this.checked==true){
					  searchState = this.value;
					  $("#total").attr("checked",false);
				  }else{
					  searchState = $("#total").value;
				  }
			  });
			
			//전체 체크
			  $("#total").change(function(){
				  if(this.checked==true){
					  searchState = this.value;
					  $("#answer").attr("checked",false);
				  }else{
					  searchState = $("#answer").value;
				  }
			  });
			
		      //검색
			  $("#search").click(function(){
				  $("#accordion2 .qaList").remove();
				  pageCnt = 0;
				  qaList();
			  });
			
			  //더보기
			  $("#moreBtn").click(function(){
				  //console.log("더보기 클릭 " + pageCnt);
				  if(!pageEnd){
					  qaList();
				  } else {
					  alert("마지막입니다");
				  }
			  });
		});
		
		//답변 저장
		function saveAnswer(obj){
			if($(obj).prev().val().length<=0){
				alert("답변 내용을 입력해주세요.");
			}else{
				answerState="answerState"+$(obj).prev().attr('id');
				saveAnswerAjax($(obj).prev().attr('id'), $(obj).prev().val());
			}
		}
		
		//답변 저장하기
		var saveAnswerAjax = function(qa_seq, answer) {
			$.ajax({
				url : rootPath + "/qa/modify.do",
				type : "POST",
				data : {
					qa_seq : qa_seq,
					answer : answer
				},
				success : answerSuccess,
				error : errorCallback
			})
		}
		
		//답변 저장하기
		var answerSuccess = function(resultData) {
			if(resultData=="success"){
				$("#"+answerState).removeClass();
				$("#"+answerState).addClass("fa fa-check text-success text");
				$("#"+answerState).text("답변완료");
				alert("저장 되었습니다.");
			}else{
				alert("저장에 실패 하였습니다. 다시 시도해 주세요.");
			}
		};

		//목록 읽어오기
		var qaList = function() {
			$.ajax({
				url : rootPath + "/qa/list.do",
				type : "POST",
				data : {
					pageCnt : pageCnt,
					searchState : searchState
				},
				success : listSuccess,
				error : errorCallback
			})
		}

		//문의하기 리스트 조회
		var listSuccess = function(resultData) {
			$.each(resultData, function(index, item) {
				var sysdate = new Date(item.writng_de);
				var sbmTr = sbmHtmlTemplate.makesbmTr(index, item);
				$("#accordion2").append(sbmTr);
			});
			pageCnt++;

			if (resultData.length < 10) {
				pageEnd = true;
			}
		};
		
		//테이블 생성 html
		var sbmHtmlTemplate = {
			  makesbmTr : function(index, item){
				  var sysdate = new Date(item.writng_de);
				  var sbmTr = '<div class="panel panel-default qaList"><div class="panel-heading nav">'
				                   + '<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapse'+index+'">'
				                   + '<span class="col-sm-2">'+formatDate(sysdate) +'</span>'
				                   + '<span class="col-sm-2">'+item.name+'</span>'
				                   + '<span class="col-sm-6">'+item.title+'</span>'
				                   + '<span class="col-sm-2">'
				                   + (item.answer!=null?'<i class="fa fa-check text-success text" id="answerState'+item.qa_seq+'">답변완료</i>':'<i class="fa fa-times text-danger text" id="answerState'+item.qa_seq+'">답변대기</i>')
				                   + '</span></a></div><div id="collapse'+index+'" class="panel-collapse collapse">'
				                   + '<div class="panel-body text-sm">'+item.contents+'</div>'
				                   + '<div class="panel-body text-sm"><h5>답변 내용</h5><div>'
				                   + '<textarea class="col-sm-11 min-h-xs" id="'+item.qa_seq+'">'+(item.answer==null?'':item.answer)+'</textarea>'
				                   + '<button class="btn btn-sm btn-danger col-sm-1" type="button" onclick="javascript:saveAnswer(this);">답변저장</button></div></div></div></div>';	
	          return sbmTr;
			  }
		  }

		//Ajax 에러 콜백함수
		var errorCallback = function() {
			alert("수행 중 오류가 발생했습니다");
		};
		
		//시간포맷
		  var formatDate = function(dateObj){
		  	var curr_year = dateObj.getFullYear();
		  	var curr_month = dateObj.getMonth() + 1;
		  	var curr_date = dateObj.getDate();
		  	var curr_hr = dateObj.getHours();
		  	var curr_min = dateObj.getMinutes();
		  	
		  	if(curr_month.toString().length == 1) {
		  		curr_month = '0' + curr_month;
		  	}
		  	if(curr_date.toString().length == 1){
		  		curr_date = '0' + curr_date;
		  	}
		  	if(curr_hr.toString().length == 1){
		  		curr_hr = '0' + curr_hr;
		  	}
		  	if(curr_min.toString().length == 1){
		  		curr_min = '0' + curr_min;
		  	}
		  	
		  	//return curr_year + "-"+ curr_month + "-" + curr_date + "  " + curr_hr + ":" + curr_min;
		  	return curr_year + "/"+ curr_month + "/" + curr_date;
		  }
	</script>
</body>
</html>