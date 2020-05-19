<?php
    $con = mysqli_connect('localhost', 'qwer456t', 'dlscjs31!!A', 'qwer456t');
     $userID = $_POST["userID"];
     $statement = mysqli_prepare($con, "DELETE FROM USER WHERE userID = ?");
     mysqli_stmt_bind_param($statement, "s", $userID);
     mysqli_stmt_execute($statement);
     //배열 선언 후
     $response = array();
     //success에 true라는 값을 넣어줌
     $response["success"] = true;
     echo json_encode($response);
?>

