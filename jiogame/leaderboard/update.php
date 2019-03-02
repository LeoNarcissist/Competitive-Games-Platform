<?php
# Its servicing in JSON format
# header('Content-Type: application/json');

ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

$sqlconnector_script = '../sql_connector.php';

# Extract GET values
$param = array();
foreach ($_GET as $name => $value) {
  $param[$name] = $value;
}

# API  Format
# localhost/jiogame/leaderboard/update.php?uid=1&gamename=chess&round=1&score=47500
# var_dump($param);

# Include the database handler
require_once($sqlconnector_script);

$insert_user = "INSERT INTO scores(uid, gamename, round, score) VALUES(
    NULLIF('{$param['uid']}',''), 
    NULLIF('{$param['gamename']}',''), 
    NULLIF('{$param['round']}',''),
    NULLIF('{$param['score']}',''))
    ON DUPLICATE KEY UPDATE
    score = NULLIF('{$param['score']}','');";


if ($dbc->query($insert_user))
  ;
else
  echo $dbc->error;

$dbc->close();

?>