# OPEN JSON
<pre><code>
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
</code></pre>

# LOGIN JSON
<pre><code>
{
  "LOGIN": [
    {
      "SID": "201131046",
      "PW": "mypasswd"
    }
  ]
}
</code></pre>

# LOGIN_RESULT JSON
<pre><code>
{
  "LOGIN_RESULT": [
    {
      "LOGFLAG": "TRUE OR FALSE",
      "NAME": "공원배"
    }
  ]
}
</code></pre>

# SEARCH JSON
<pre><code>
{
  "SEARCH_RESULT": [
    {
      "SUBJECT_NUM": "2",
      "SUBJECT": [
        {
          "CLASS_NO": "01",
          "CLASS_ROOM": "d302",
          "CLASS_NAME": "모바일앱",
          "CLASS_ID": "AN0044",
          "WEEK": "3",
          "CTIME": "2"
        },
        {
          "CLASS_NO": "01",
          "CLASS_ROOM": "a607",
          "CLASS_NAME": "프로젝트설계",
          "CLASS_ID": "NA7028",
          "WEEK": "3",
          "CTIME": "2"
        }
      ]
    }
  ]
}
</code></pre>

# BEACON JSON 
<pre><code>
 {
  " BEACON": [
    {
      " SID": " 201131046",
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
</code></pre>

# ATTEND JSON (server -> android)
<pre><code>
 {
  "ATTEND": [
    {
      "TIME": "yy:mm:dd:HH:MM:SS",
      "BEACON_FLAG": "1/2/3/",           //1(비콘을찾음), 2(거리밖에 비콘 감지), 3(비콘을 찾을수 없음)
      "ATTEND_FLAG": "00/01/10/11",   //00(출석), 01(지각), 10(유고), 11(결석)
      "CLASS_CODE": "A1818",
      "CLASS_NO": "01",
      "CLASS_NAME": "프로젝트설계",
      "CTIME": "1/2/3",
      "WEEK": "11"
    }
  ]
}
</code></pre>
