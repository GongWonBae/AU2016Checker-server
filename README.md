OPEN JSON (web->server)

{
  "OPEN": [
    {
      "CODE": "AN1234",
      "ROOM": "A306",
      "CLASS_NO": "02",
      "S_TIME": "time",
      "WEEK": "3",
      "CTIME": "1"
    }
  ]
}

LOGIN JSON (android -> server)

{
  "LOGIN": [
    {
      "SID": "201131046",
      "PW": "mypasswd"
    }
  ]
}

LOGIN_RESULT JSON (server -> android)

{
  "LOGIN_RESULT": [
    {
      "LOGFLAG": "TRUE OR FALSE",
      "NAME": "������"
    }
  ]
}

SEARCH JSON (android -> server)

{
  "SEARCH": [
    {
      "SID": "201131046"
    }
  ]
}


SEARCH_RESULT JSON (server -> android)

{
  "SEARCH_RESULT": [
    {
      "SUBJECT_NUM": "2",
      "SUBJECT": [
        {
          "CLASS_NO": "01",
          "CLASS_ROOM": "d302",
          "CLASS_NAME": "����Ͼ�",
          "CLASS_ID": "AN0044",
          "WEEK": "3",
          "CTIME": "2"
        },
        {
          "CLASS_NO": "01",
          "CLASS_ROOM": "a607",
          "CLASS_NAME": "������Ʈ����",
          "CLASS_ID": "NA7028",
          "WEEK": "3",
          "CTIME": "2"
        }
      ]
    }
  ]
}

BEACON JSON (android -> server)

 {
  " BEACON": [
    {
      " CLASS_CODE": " AN0044",
      " CLASS_NO": " 01",
      " CLASSROOM": " A607",
      " WEEK": " 13",
      " CTIME": "3",
      " BEACON_CNT": "4",
      " BEACON_INFO": [
        {
          " UUID": " e2c56db5-dffb-48d2-b060-d0f5a71096e0",
          " MAJOR": " 1000",
          " MINOR": " 2000",
          " DISTANCE": " 15"
        },
        {
          " UUID": " 2221",
          " MAJOR": " 2222",
          " MINOR": " 2223",
          " DISTANCE": " 2224"
        },
        {
          " UUID": " 3331",
          " MAJOR": " 3332",
          " MINOR": " 3333",
          " DISTANCE": " 3334"
        },
        {
          " UUID": " 4441",
          " MAJOR": " 4442",
          " MINOR": " 4443",
          " DISTANCE": " 4444"
        }
      ]
    }
  ]
}


ATTEND JSON (server -> android)

 {
  "ATTEND": [
    {
      "TIME": "yy:mm:dd:HH:MM:SS",
      "BEACON_FLAG": "1/2/3/",           //1(������ã��), 2(�Ÿ��ۿ� ���� ����), 3(������ ã���� ����)
      "ATTEND_FLAG": "00/01/10/11",   //00(�⼮), 01(����), 10(����), 11(�Ἦ)
      "CLASS_CODE": "A1818",
      "CLASS_NO": "01",
      "CLASS_NAME": "������Ʈ����",
      "CTIME": "1/2/3",
      "WEEK": "11"
    }
  ]
}