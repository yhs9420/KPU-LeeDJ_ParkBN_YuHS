<?php
    $con = mysqli_connect('localhost', 'qwer456t', 'dlscjs31!!A', 'qwer456t');
     $userID = $_POST["userID"];
     $recipeID = $_POST["recipeID"];

     //$statement = mysqli_prepare($con, "DELETE FROM MYRECIPE WHERE userID = '$userID' AND recipeID = '$recipeID'");     
     //mysqli_stmt_bind_param($statement, "si", $userID , $recipeID);
     //mysqli_stmt_execute($statement);

     $statement = mysqli_query($con, "DELETE FROM MYRECIPE WHERE userID = '$userID' AND recipe_id = '$recipeID'");

     //배열 선언 후
     $response = array();
     //success에 true라는 값을 넣어줌
     $response["success"] = true;
     echo json_encode($response);
?>

