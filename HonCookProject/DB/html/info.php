<?php 
    $host = 'localhost';

    $user = 'root';

    $pw = 'dlscjs31';

    $dbName = 'test';

    $mysqli = new mysqli($host, $user, $pw, $dbName);



    if($mysqli){

        echo "MySQL 접속 성공";

    }else{

        echo "MySQL 접속 실패";

    }
?>
