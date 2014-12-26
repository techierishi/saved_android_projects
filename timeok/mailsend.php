<?php
error_reporting(E_ALL);


if(isset($_POST['emaildata'])){
	$admin_email = "noreply<Admin@gmail.com>";

	
	$emaildata = preg_replace('/\\\"/',"\"", $_POST['emaildata']);
	
	dbgg($emaildata,'a');
	
	$emaildata = json_decode($emaildata);

	dbgg($emaildata,'a');
	
	foreach ($emaildata->emails as $data1){    
		$myArray[] = $data1;
	}
	
	$email_list = implode(" ,", $myArray);
	$subject = "Contact Us";
	$from = $admin_email;
	$to = $email_list;
	$headers = 'MIME-Version: 1.0' . "\r\n";
	$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
	$headers .= 'From: noreply<admin@gamil.com>' . "\r\n";
	
	$urllink = stripslashes($emaildata->urllink);
		
	$message = $emaildata->body . '<br>' . $urllink;
	
	$bool = mail($to, $subject, $message, $headers);
	
	if($bool){
		$response["success"] = 1;
        $response["message"] = "Mail Send";
	}else{
		$response["success"] = 0;
        $response["message"] = "Error";
	}
	
	header('Content-Type: application/json');
	exit(json_encode($response));

}

function dbgg($msg,$md='w',$fn='dbgg.html',$tg='No tag'){
	
	$fo = fopen($fn,$md);
	
	fwrite($fo,'<br>[================[ '. date('d/m/Y H:m:s').' { '.$tg. ' } ]================]<br>');
	$msg = "<pre>".print_r($msg,true)."</pre>";
	fwrite($fo,$msg);
	fclose($fo);
	
}
?>