<?php

    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
    $con=mysqli_connect( "localhost", "qwer456t", "dlscjs31!!A", "qwer456t"); 
	$result = mysqli_query($con, "SELECT * FROM USER;");
	$response = array();//배열 선언

	while($row = mysqli_fetch_array($result)){
		array_push($response, array("userID"=>$row[0], "userPassword"=>$row[1], "userName"=>$row[2], "userAge"=>$row[3]));
	}
	//response라는 변수명으로 JSON 타입으로 $response 내용을 출력

	echo json_encode(array("response"=>$response));	
	mysqli_close($con);
?>
