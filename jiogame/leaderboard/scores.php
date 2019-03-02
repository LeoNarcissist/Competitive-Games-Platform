<?php
# Its servicing in JSON format
header('Content-Type: application/json');

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
# localhost/jiogame/leaderboard/scores.php?gamename=chess&top=10
# var_dump($param);


if (isset($param['top']))
    $top = $param['top'];
else
    $top = False;

# var_dump($top);

# Include the database handler
require_once($sqlconnector_script);

$ongoing_round = "SELECT start,round FROM timings WHERE
    gamename='{$param['gamename']}' AND
    start < (SELECT CONVERT_TZ(now(),@@session.time_zone,'+05:30') AS now)
    ORDER BY start DESC
    LIMIT 1;";

if ($ongoing = $dbc->query($ongoing_round)) {
    if ($ongoing->num_rows == 1) 
        while($row = $ongoing->fetch_object()) {
            $round = $row->round; # Fetch current ongoing round
            $start = $row->start;
        }
}

if($top != False)
    $select_score = "SELECT sc.*, us.fname, us.lname FROM scores sc
    INNER JOIN users us ON sc.uid=us.uid
    WHERE gamename='{$param['gamename']}' AND
    round='{$round}'
    ORDER BY score DESC
    LIMIT {$top};";
else
    $select_score = "SELECT sc.*, us.fname, us.lname FROM scores sc
    INNER JOIN users us ON sc.uid=us.uid
    WHERE gamename='{$param['gamename']}' AND
    round='{$round}'
    ORDER BY score DESC;";

$scoreboard_array = array();
if ($person = $dbc->query($select_score)) {
    if ($person->num_rows > 0) 
        while($row = $person->fetch_object())
            array_push($scoreboard_array, $row);
}

echo json_encode($scoreboard_array);

$dbc->close();

?>