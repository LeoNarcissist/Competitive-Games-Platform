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

# API Format
# localhost/jiogame/leaderboard/play.php?uid=1&gamename=chess
# var_dump($param);


# Include the database handler
require_once($sqlconnector_script);

# Current datetime
$current_time = "SELECT CONVERT_TZ(now(),@@session.time_zone,'+05:30') AS now;";

if ($person = $dbc->query($current_time)) {
    if ($person->num_rows == 1) 
        while($row = $person->fetch_object())
            $now = $row->now;
}

# Find correct game start datetime and its round
$ongoing_round = "SELECT start,round FROM timings WHERE
    gamename='{$param['gamename']}' AND
    start < (SELECT CONVERT_TZ(now(),@@session.time_zone,'+05:30') AS now)
    ORDER BY start DESC
    LIMIT 1;";

$next_round = "SELECT start,round FROM timings WHERE
    gamename='{$param['gamename']}' AND
    start > (SELECT CONVERT_TZ(now(),@@session.time_zone,'+05:30') AS now)
    ORDER BY start
    LIMIT 1;";

if ($ongoing = $dbc->query($ongoing_round)) {
    if ($ongoing->num_rows == 1) 
        while($row = $ongoing->fetch_object()) {
            $currentround = $row->round; # Fetch current ongoing round
            $currentstart = $row->start;
        }
}

# Has the guy played once before?
# Has the guy been eliminated?
$select_elig = "SELECT * FROM scores
    WHERE 
    uid='{$param['uid']}' AND
    gamename='{$param['gamename']}' AND
    round='{$currentround}';";

if ($person = $dbc->query($select_elig)) {
    if ($person->num_rows == 1) {
        # Guy has already played
        # Show details of next round
        $allow = "0";
        $nextround = "0";
        $nextstart = "over";

        if ($next = $dbc->query($next_round)) {
            if ($next->num_rows == 1) 
                while($row = $next->fetch_object()) {
                    $nextround = $row->round; # Fetch current ongoing round
                    $nextstart = $row->start;
                }
        }

        $round = $nextround;
        $start = $nextstart;
    }
    else {
        # Allow him to play this round but...
        $allow = "0";

        $round = $currentround;
        $start = $currentstart;

        # Only if he's qualified from prev round
        $prevround = $round - 1;
        # Get total user count
        $user_count = "SELECT COUNT(*) AS ct FROM scores WHERE round={$prevround};";

        if ($num_users = $dbc->query($user_count)) {
            if ($num_users->num_rows > 0) 
                while($row = $num_users->fetch_array(MYSQLI_ASSOC)) {
                    $count = $row['ct'];
                    $count = intdiv($count, 2);
                }
        }

        # echo $count;

        # Get top half
        $top_half = "SELECT * FROM scores WHERE round={$prevround}
        ORDER BY score DESC LIMIT 0, {$count};";

        $hhh = array();
        if ($half_users = $dbc->query($top_half)) {
            if ($half_users->num_rows > 0) 
                while($row = $half_users->fetch_object()) {
                    if ($param['uid'] == $row->uid)
                        $allow = "1"; # Only then allow
                }
        }
    }
}

# Generate json
class user_play_response{
    public $round;
    public $start;
    public $now;
    public $allow;
    function __construct($round, $start, $now, $allow){
        $this->round = $round;
        $this->start = $start;
        $this->now = $now;
        $this->allow = $allow;
    }
}
$jsonvar = new user_play_response($round, $start, $now, $allow);

echo json_encode($jsonvar);

$dbc->close();

?>