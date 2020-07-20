<?php

   $con = mysqli_connect('localhost', 'qwer456t', 'dlscjs31!!A', 'qwer456t');
     $userID = $_POST["userID"];

     $statement = mysqli_prepare($con, "SELECT userID FROM USER WHERE userID = ?");
     //위에서 *로 하면 mysqli_stmt_bind_result에서 에러가 나서 정정함

     mysqli_stmt_bind_param($statement, "s", $userID);
     mysqli_stmt_execute($statement);
     mysqli_stmt_store_result($statement);//결과를 클라이언트에 저장함
     mysqli_stmt_bind_result($statement, $userID);//결과를 $userID에 바인딩함

     $response = array();
     $response["success"] = true;

     while(mysqli_stmt_fetch($statement)){
       $response["success"] = false;//회원가입불가를 나타냄
       $response["userID"] = $userID;
     }

     //데이터베이스 작업이 성공 혹은 실패한것을 알려줌
     echo json_encode($response);

?>
