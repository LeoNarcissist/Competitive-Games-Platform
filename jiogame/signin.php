<?php
# Its servicing in JSON format
header('Content-Type: application/json');

ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

$sqlconnector_script = './sql_connector.php';

# Extract GET values
$param = array();
foreach ($_GET as $name => $value) {
  $param[$name] = $value;
}

# API Format
# localhost/jiogame/signin.php?email=ben@dover.com&passwd=pass@123

# var_dump($param);

# Include the database handler
require_once($sqlconnector_script);

$hashpwd = md5($param['passwd']);

$select_user = "SELECT uid,email,passwd FROM users
WHERE email = '{$param['email']}' AND passwd = '{$hashpwd}' 
;";


$uid = null;
if ($person = $dbc->query($select_user)) {
    if ($person->num_rows == 1) 
        while($row = $person->fetch_array(MYSQLI_ASSOC))
            $uid = intval($row["uid"]);
}

# Generate json
class userid{
    public $uid;
    function __construct($uid){
        $this->uid = $uid;
    }
}
$jsonvar = new userid($uid);

echo json_encode($jsonvar);

$dbc->close();

?>
