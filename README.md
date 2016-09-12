# AU2016Checker-serverLogin Json 
{
	"LOGIN" : [
		{ "SID" : “201131046”,
	  	“PW" : "mypasswd" ,
		}
		]
}

Result_001
 TRUE / FALSE 


SEARCH JSON 

{
	"SEARCH" : [
		{ SID : 201131046
		}
	]
}




















Result_002 
{
	"SEARCH_RESULT" : [
		{
			“SUBJECT_NUM" : "듣는 과목 수”
			"SUBJECT" : [ 
				 { “NAME" : ”프로젝트설계“,
				   “CODE" : ”A1818",
				   "HAKJUM" : "3",
				   "PROFESSER":“상홍”,
				   “1th" : "출”/“결”/“지”/“유고”,/"NULL"  ,
					......"15th" : "출”/“결”/“지”/“유고”,/"NULL"  , 
				},
				 { “NAME" : ”프로젝트설계“,
				   “CODE" : ”A1818",
				   "HAKJUM" : "3",
				   "PROFESSER":“상홍”,
				   “1th" : "출”/“결”/“지”/“유고”,/"NULL" ,
				......"15th" : "출”/“결”/“지”/“유고”,/"NULL"  , 
				}
			]
		}
	]
}	
			















Beacon Json
{ "BEACON" : [ 
		{
		  "id" : "2011xxxxx"
		  "phone" : "01012345678"
		  "nowtime" : "16/05/07/13/57"
		  "beacon_cnt" : "4"  	//maximum : 4개 minimum : 0개
		  "beacon_info" : [
					{ "uuid" : "#@!$%T@!@#",	
					  "major": "12345",
					  "minor": "67890",
					  "distance" : 10
					},
					{ "uuid" : "#@!$%T@!@#",
					  "major": "12345",
					  "minor": "67890",
					  "distance" : 10
					},
					{ "uuid" : "#@!$%T@!@#",
					  "major": "12345",
					  "minor": "67890",
					  "distance" : 10
					},
					{ "uuid" : "#@!$%T@!@#",
					  "major": "12345",
					  "minor": "67890",
					  "distance" : 10
					},
				 ]
		}
	 ]
}










	
Result_003
	
{ "ATTEND" : [
		{ "TIME" : "yy:mm:dd:HH:MM:SS",
		  "RESULT" : "출“/”결“/”지“/”다시시도하셈“,
		  “NOW_SUBJECT" : ”A1818“,
		  "SUBJECT_NAME" : "프설프설“, 
		  “SUBJECT_ROOM" : ”A301",
		}
	]
}
			
출석 가능 Json
	
{
	"SEARCH" : [
		{
			“SID” : "201131046”
		}
	]
}	
	

출석 가능 체크 후 보내주는 Json
	
{
	"SEARCH_RESULT" : [
		{
			“SUBJECT_NUM" : "열려있는 과목 수”
			"SUBJECT" : 
			[ 
				 { “NAME" : ”프로젝트설계“,
				   “CODE" : ”A1818",
				   "CLASS_NO" : "01",
				},
				 { “NAME" : ”프로젝트설계“,
				   “CODE" : ”A1818",
				   "CLASS_NO" : "01",
				},
			]
		}
	]
}	
				
