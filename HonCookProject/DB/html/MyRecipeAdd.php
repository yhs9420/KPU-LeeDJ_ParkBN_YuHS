<?php

 $con = mysqli_connect('localhost', 'qwer456t', 'dlscjs31!!A', 'qwer456t');
   if (!$con)
  {
     echo "MySQL 접속 에러 : ";
     echo mysqli_connect_error();
     exit();
  }

   //post방식으로 데이터를 받는다.
    $userID = $_POST["userID"];
    $recipe_id = $_POST["recipeID"];


    //데이터베이스에 값을 넣는부분
    $statement = mysqli_prepare($con, "INSERT INTO MYRECIPE VALUES(?, ?)");
    mysqli_stmt_bind_param($statement, "si", $userID, $recipe_id);
    mysqli_stmt_execute($statement);

    //배열 선언후
    $response = array();
    $response["success"] = true;//success라는 인덱스에 true값을 넣어줌

    echo json_encode($response);//JSON형식으로 출력

?>
