<?php
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
    $con=mysqli_connect( "localhost", "qwer456t", "dlscjs31!!A", "qwer456t");

     $userID = $_POST["userID"];
     $userPassword = $_POST["userPassword"];

     $statement = mysqli_prepare($con, "SELECT * FROM USER WHERE userID = ? AND userPassword = ?");
     mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
     mysqli_stmt_execute($statement);
     mysqli_stmt_store_result($statement);
     mysqli_stmt_bind_result($statement, $userID, $userPassword, $userName, $userAge);

     $response = array();
     $response["success"] = false;

     while(mysqli_stmt_fetch($statement)){
      $response["success"] = true;
      $response["userID"] = $userID;
      $response["userPassword"] = $userPassword;
      $response["userName"] = $userName;
      $response["userAge"] = $userAge;
     }

     echo json_encode($response);
?>

