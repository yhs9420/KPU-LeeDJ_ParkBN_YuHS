<?php

    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
    $con=mysqli_connect( "localhost", "qwer456t", "dlscjs31!!A", "qwer456t"); 
	$result = mysqli_query($con, "SELECT USER.userID, USER.userAge, Recipe.recipe_id, Recipe.recipe_name, Recipe.recipe_category, Recipe.recipe_ingredient, Recipe.recipe_content
FROM Recipe, MYRECIPE, USER
WHERE MYRECIPE.recipe_id = Recipe.recipe_id
AND USER.userID = MYRECIPE.userID;");
	$response = array();//배열 선언

	while($row = mysqli_fetch_array($result)){
		array_push($response, array("userID"=>$row[0], "userAge"=>$row[1], "recipe_id"=>$row[2], "recipe_name"=>$row[3], "recipe_category"=>$row[4], "recipe_ingredient"=>$row[5], "recipe_content"=>$row[6]));
	}
	//response라는 변수명으로 JSON 타입으로 $response 내용을 출력

	echo json_encode(array("response"=>$response));	
	mysqli_close($con);


?>
