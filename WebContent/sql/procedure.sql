  /******************************************************************************
  3查询任务列表信息
******************************************************************************/
  PROCEDURE PROC_QUERYTASK(
            IN_QRYBEGINPOS        IN  VARCHAR2,    --翻页开始位置
            IN_QRYNUM             IN  VARCHAR2,    --翻页查询条数
            IN_DATE_TYPE          IN  VARCHAR2,    --日期种类
            IN_DATE_B             IN  VARCHAR2,    --开始日期
            IN_DATE_E             IN  VARCHAR2,    --截止日期
            IN_APP_NO             IN  VARCHAR2,    --申请号
            IN_BO_NAME            IN  VARCHAR2,    --借款人姓名
            IN_BO_ID              IN  VARCHAR2,    --借款人证件号
            IN_SALES_NAME         IN  VARCHAR2,    --销售员名
            IN_BRAND_ID           IN  VARCHAR2,    --APP_NO
            IN_REGION_ID          IN  VARCHAR2,    --品牌ID
            IN_CITY_NAME          IN  VARCHAR2,    --大区ID
            IN_DISTRIBUTOR_ID     IN  VARCHAR2,    --承销商ID
            IN_DEALER_NAME        IN  VARCHAR2,    --经销商名
            IN_DEALER_ID          IN  VARCHAR2,    --经销商ID add by lzm 修改一个经销商多账户，打回任务的bug
            IN_IS_USED            IN VARCHAR2,     --是否查询二手车业务标识 为屏蔽门户
            IN_POSITION_ID        IN  VARCHAR2,    --岗位代码
            IN_WORKER             IN  VARCHAR2,    --操作员姓名
            in_undoState          IN  VARCHAR2,    --选中未处理状态
            in_indoState          IN  VARCHAR2,    --选中正在处理中状态
            in_preSubmitState     IN  VARCHAR2,    --选中预提交状态
            in_suspendState       IN  VARCHAR2,    --选中挂起状态
            in_doneState          IN  VARCHAR2,    --选中已完结状态
            in_terminateState     IN  VARCHAR2,    --选中已完成状态
            in_refuseState        IN  VARCHAR2,    --选中已拒绝状态
            in_inactive           IN  VARCHAR2,    --选中未激活状态  add by zr
            IN_ORDER_TYPE         IN  VARCHAR2,    --选择按哪一行进行排序
            IN_ORDER_WAY_TYPE     IN  VARCHAR2,    --选择排序方式
            IN_CO_LOAN_FLAG       IN VARCHAR2,     --贷款方式 lzm
            IN_CO_BANK_NAME       IN VARCHAR2,  --选择联合贷款合作银行名称--
            IN_CAR_TYPE           IN VARCHAR2,     --车辆类型 add BY FAN_GJUN
            IN_USER_ID			      IN VARCHAR2,     --当前处理人 id   add by zr 
            IN_PRI_TYPE           IN VARCHAR2,     --流程类型  只有审贷流程设置此值，为了区分排序方式  add by lzm 
            IN_PREAPPROVE         IN VARCHAR2,     --APP手机端接口预审批状态  add by yxb
            OUT_TOTALNUM          OUT VARCHAR2,   --记录总数
            OUT_UNDONUM           OUT VARCHAR2,   --记录未处理任务总数（改动）
            OUT_INDONUM           OUT VARCHAR2,   --记录处理中任务总数（改动）
            OUT_RETCODE           OUT VARCHAR2,   --错误代码
            OUT_MESG              OUT VARCHAR2,   --错误信息
            OUT_TASKCUR           OUT REF_SETCUR,  --任务列表记录集
            OUT_BRANDCUR          OUT REF_SETCUR,  --品牌列表记录集
            OUT_REGIONCUR         OUT REF_SETCUR,  --大区列表记录集
            OUT_DISTRIBUTORCUR    OUT REF_SETCUR,  --承销商列表记录集
            OUT_POSITIONCUR       OUT REF_SETCUR,  --岗位列表记录集
            OUT_BANKCUR           OUT REF_SETCUR   --联贷银行列表记录集
   ) IS

   V_SQL_PAGE                     VARCHAR2(8000);--翻页用的
   V_SQL_ALL                      VARCHAR2(8000);--翻页用的
   V_SQL_COUNT                    VARCHAR2(10000);--求总数用
   V_SQL_UNDOCOUNT                VARCHAR2(10000);--求未处理任务总数用（改动）
   V_SQL_INDOCOUNT                VARCHAR2(10000);--求处理中任务总数用（改动）
   V_SELECT_PAGE                  VARCHAR2(11000);--翻页开头
   V_ORDER_PAGE                   VARCHAR2(8000);--翻页开头
   V_SELECT                       VARCHAR2(8000);--所需字段
   V_SELECT_ORDER                 VARCHAR2(8000);--分页排序
   V_FROM                         VARCHAR2(11000);--相关表
   V_WHERE                        VARCHAR2(8000);--动态条件
   V_GROUP                        VARCHAR2(200);
   V_ORDER                        VARCHAR2(200);
   V_WHERE_PAGE                   VARCHAR2(8000);--翻页结束
   V_TEMP_DATE                    VARCHAR2(1000);--子查询_任务日期
   V_TEMP_TASK                    VARCHAR2(1000);--子查询_任务子句
   V_TEMP_COND                    VARCHAR2(1000);--子查询_任务条件
   V_TEMP_FLAG                    VARCHAR2(1):=0;--是否任务子查询标志
   V_STATUS_FLAG                  VARCHAR2(1):=0;--是否任务状态查询标志
   V_STATUS_COND                  VARCHAR2(1000);--子查询_任务状态条件 modify by yyh
   V_create_table                 VARCHAR2(8000);--创建临时表

   V_COUNT_SET                    REF_SETCUR;--存放总数的临时游标
   V_UNDOCOUNT_SET                REF_SETCUR;--存放未处理任务总数的临时游标(改动)
   V_INDOCOUNT_SET                REF_SETCUR;--存放处理中任务总数的临时游标(改动)
   BEGIN
      OUT_RETCODE := '0';

      --生成临时表，存放本面taskId
      V_SELECT := 'SELECT
                   TASK_ID,
                   loan_grade,
                   pos_rcv_datim,
                   TASKDATIM , ';


IF IN_PRI_TYPE IS NOT NULL AND IN_PRI_TYPE='1' THEN
     IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='2'THEN
           V_SELECT_ORDER := 'row_number() over (order by PRI_TYPE desc,UPGRADE_FLAG desc,CREDITGRADE asc,URGENT_FLAG desc, loan_grade desc, ANTIFRAUD_FLAG desc, TASKDATIM '||IN_ORDER_WAY_TYPE||' ) RN';
         ELSIF IN_ORDER_TYPE = '1' THEN   
           V_SELECT_ORDER := 'row_number() over (order by PRI_TYPE desc,UPGRADE_FLAG desc,CREDITGRADE asc,URGENT_FLAG desc, loan_grade desc, ANTIFRAUD_FLAG desc,  pos_rcv_datim '||IN_ORDER_WAY_TYPE||' ) RN';
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_SELECT_ORDER := ' row_number() over (order by PRI_TYPE desc,UPGRADE_FLAG desc,CREDITGRADE asc,URGENT_FLAG desc, loan_grade desc, ANTIFRAUD_FLAG desc, pos_rcv_datim ASC) RN ';
      END IF;
      
 ELSE 
   IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='2'THEN
           V_SELECT_ORDER := 'row_number() over (order by  DY_FLAG asc,URGENT_FLAG desc, loan_grade desc,TASKDATIM '||IN_ORDER_WAY_TYPE||' ) RN';
         ELSIF IN_ORDER_TYPE = '1' THEN   
           V_SELECT_ORDER := 'row_number() over (order by  DY_FLAG asc,URGENT_FLAG desc, loan_grade desc, pos_rcv_datim '||IN_ORDER_WAY_TYPE||' ) RN';
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_SELECT_ORDER := ' row_number() over (order by  DY_FLAG asc,URGENT_FLAG desc, loan_grade desc, pos_rcv_datim ASC) RN ';
      END IF;
      
 END IF;

      V_SELECT := V_SELECT||V_SELECT_ORDER;

      --加入关联表
      V_FROM := ' FROM  (  select
                          distinct( tt.task_id) TASK_ID ,
                           l.loan_grade,
                           pd4.pos_rcv_datim TASKDATIM,
                           T.URGENT_FLAG,
                           T.ANTIFRAUD_FLAG,
                           T.PRI_TYPE,
                           T.DY_FLAG ,
                           T.CONDITION_FLAG,
                           L.UPGRADE_FLAG,
                           L.CREDITGRADE,
                           case when PD.pos_rcv_datim is null
                                then ( case when PD3.num <> 0 then
                                    PD2.pos_rcv_datim
                                 else PD4.pos_rcv_datim end )
                            else  PD.pos_rcv_datim
                           end as  pos_rcv_datim

                            FROM  task_track tt,
                                   TASK T,
                                ( select tt.task_id  TASK_ID,
                                   max(tt.pos_submit_datim) pos_rcv_datim
                                  from task_track tt
                                  where tt.action_type <> ''3''
                                  group by tt.task_id
                               ) pd,
                               (
                                  select tt.task_id,
                                         tt.position_id,
                                         max(tt.pos_rcv_datim) pos_rcv_datim
                                  from task_track tt
                                  group by tt.task_id, tt.position_id
                               ) pd2,
                               ( select count(*) as num ,tt.task_id
                                from task_track tt
                                where
                                    tt.action_type<>''3'' and  tt.action_type is not null
                                    group by tt.task_id
                                  )pd3,
                                (
                                  select tt.task_id,
                                         min(tt.pos_rcv_datim) pos_rcv_datim
                                  from task_track tt
                                  group by tt.task_id
                                 ) pd4,
                                 ETP_USER U,
                                 POSITION P,
                                 LOAN L,
                                 DEALER_LOAN_MAP DLM,
                                 DEALER D,
                                 CITY C,
                                 REGION R,
                                 BRAND_DEALER_MAP BDM,
                                 BRAND B,
                                 DISTRIBUTOR_DEALER_MAP DDM,
                                 DISTRIBUTOR DT,
                                 APPLICATION_INFO AI  ';
      --动态拼条件
      V_WHERE := ' WHERE tt.task_id = t.task_id
                         and T.LOAN_ID = L.LOAN_ID
                         AND AI.APPLICATION_NUMBER = L.APPLICATION_NO
                          AND T.TASK_ID = PD.TASK_ID(+)
                         AND T.POSITION_ID = PD2.POSITION_ID
                         AND T.TASK_ID = PD2.TASK_ID
                         AND T.TASK_ID = PD3.TASK_ID(+)
                         AND T.TASK_ID = PD4.TASK_ID
                         AND T.USERID = U.USERID(+)
                         AND T.POSITION_ID = P.POSITION_ID
                         AND L.LOAN_ID = DLM.LOAN_ID(+)
                         AND DLM.DEALER_ID = D.DEALER_ID(+)
                         AND D.CITY_ID = C.CITY_ID(+)
                         AND D.REGION_ID = R.REGION_ID(+)
                         AND D.DEALER_ID = BDM.DEALER_ID(+)
                         AND BDM.BRAND_ID = B.BRAND_ID(+)
                         AND D.DEALER_ID = DDM.DEALER_ID(+)
                         AND DDM.DISTRIBUTOR_ID = DT.DISTRIBUTOR_ID(+)
                         AND T.PRI_TYPE NOT IN (''11'',''12'',''13'',''14'',''15'',''16'')';


      IF IN_DATE_TYPE = '1_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''1''';
      ELSIF IN_DATE_TYPE = '1_2' THEN
              V_TEMP_DATE :='TASK_END_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''1''';
      ELSIF IN_DATE_TYPE = '2_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''2''';
      ELSIF IN_DATE_TYPE = '2_2' THEN
                V_TEMP_TASK :=' AND PRI_TYPE = ''2''';
      ELSIF IN_DATE_TYPE = '3_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''3''';
      ELSIF IN_DATE_TYPE = '4_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :='';
      ELSIF IN_DATE_TYPE = '5_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''5''';
      ELSIF IN_DATE_TYPE = '6_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''6''';
      ELSIF IN_DATE_TYPE = '21_1' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
              V_TEMP_TASK :=' AND PRI_TYPE = ''21''';
      ELSIF IN_DATE_TYPE = '0_1' THEN
              V_TEMP_DATE :='pd4.pos_rcv_datim';
               V_TEMP_TASK :='';
      ELSIF IN_DATE_TYPE = '0_2' THEN
              V_TEMP_DATE :='pd2.pos_rcv_datim';
               V_TEMP_TASK :='';
      ELSIF IN_DATE_TYPE = 'selfPosition' THEN
              V_TEMP_DATE :='TASK_TFSCN_DATIM';
               V_TEMP_TASK :='';
      ELSE
        V_TEMP_DATE := 'TASK_TFSCN_DATIM'; --modify fgj 经销商日期查询报错
      END IF;
        V_WHERE := V_WHERE  || V_TEMP_TASK ;



      IF IN_APP_NO IS NOT NULL THEN --申请号
         V_WHERE := V_WHERE || ' AND L.APPLICATION_NO LIKE ''%' || IN_APP_NO ||'%''';
      END IF;
      IF IN_BO_NAME IS NOT NULL THEN --借款人姓名
         V_WHERE := V_WHERE || ' AND L.BORROWER_NAME LIKE ''%' || IN_BO_NAME ||'%''';
      END IF;
      IF IN_BO_ID IS NOT NULL THEN --借款人证件号
         V_WHERE := V_WHERE || ' AND L.BORROWER_ID LIKE ''%' || IN_BO_ID ||'%''';
      END IF;
      IF IN_SALES_NAME IS NOT NULL THEN --销售员
         V_WHERE := V_WHERE || ' AND L.SALES_NAME LIKE ''%' || IN_SALES_NAME ||'%''';
      END IF;
      IF IN_POSITION_ID IS NOT NULL THEN --岗位ID
         V_WHERE := V_WHERE || ' AND T.POSITION_ID =' || IN_POSITION_ID || '';
         --豁免初审我的任务判断  add by zr  
		     IF IN_POSITION_ID = '39' AND IN_USER_ID IS NOT NULL THEN 
		       V_WHERE := V_WHERE || ' AND (T.USERID = ''' || IN_USER_ID || ''' OR T.USERID IS NULL)';
		    END IF;
      END IF;
      --添加判断，如果position_id!=39,显示下面的拼接where条件 (豁免初审)  add by zr 
	    IF IN_POSITION_ID != '39' THEN 
         IF IN_WORKER IS NOT NULL THEN --当前操作人员
            V_WHERE := V_WHERE || ' AND U.NAME LIKE ''%' || IN_WORKER ||'%''';
            END IF;
     END IF;
      IF IN_BRAND_ID IS NOT NULL THEN --汽车品牌
         V_WHERE := V_WHERE || ' AND B.BRAND_ID =' || IN_BRAND_ID || '';
      END IF;
      IF IN_REGION_ID IS NOT NULL THEN --大区
         V_WHERE := V_WHERE || ' AND R.REGION_ID =' || IN_REGION_ID || '';
      END IF;
      IF IN_CITY_NAME IS NOT NULL THEN --城市
         V_WHERE := V_WHERE || ' AND C.CITY_NAME LIKE ''%' || IN_CITY_NAME ||'%''';
      END IF;
      IF IN_DISTRIBUTOR_ID IS NOT NULL THEN --承销商
         V_WHERE := V_WHERE || ' AND DT.DISTRIBUTOR_ID =' || IN_DISTRIBUTOR_ID || '';
      END IF;
      IF IN_DEALER_NAME IS NOT NULL THEN --经销商
         V_WHERE := V_WHERE || ' AND D.DEALER_NAME LIKE ''%' || IN_DEALER_NAME ||'%''';
      END IF;
      IF IN_DEALER_ID IS NOT NULL THEN --经销商ID  add by lzm  修改一个经销商多账户，打回任务的bug
         V_WHERE := V_WHERE || ' AND D.DEALER_ID = ' || IN_DEALER_ID ||'';
      END IF;

      IF IN_IS_USED IS NOT NULL THEN --是否查询二手门户
         V_WHERE := V_WHERE || ' AND L.ISUSED = '|| IN_IS_USED ||'';
      END IF;

      IF IN_CO_LOAN_FLAG IS NOT NULL THEN --贷款方式--
         V_WHERE := V_WHERE || ' AND L.CO_LOAN_FLAG=' || IN_CO_LOAN_FLAG || '';
      END IF;
      IF IN_CO_BANK_NAME IS NOT NULL THEN --联贷银行--
         V_WHERE := V_WHERE || ' AND L.CO_BANK_NAME LIKE ''%' || IN_CO_BANK_NAME ||'%''';
      END IF;

      IF IN_CAR_TYPE IS NOT NULL THEN --车辆类型--
         IF(IN_CAR_TYPE='1') THEN
            V_WHERE := V_WHERE || ' AND L.ISUSED=''1'' ';
         ELSIF(IN_CAR_TYPE='0') THEN
            V_WHERE := V_WHERE || ' AND L.ISUSED=''0'' ';
         END IF;
      END IF;

      V_STATUS_COND := ' AND ( 1<> 1';

      IF in_undoState IS NOT NULL THEN --选中未处理状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR (T.FLAG = ''9'' AND T.TASK_STATUS = ''1'')';
      END IF;

      IF in_indoState IS NOT NULL THEN --选中正在处理中状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR (T.FLAG = ''9'' AND T.TASK_STATUS = ''2'')';
      END IF;

      IF  in_preSubmitState  IS NOT NULL THEN --选中预提交状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR T.FLAG = ''11'' OR T.TASK_STATUS = ''7''';
      END IF;

      IF in_suspendState IS NOT NULL THEN --选中挂起状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR T.FLAG = ''6'' OR T.TASK_STATUS = ''3''';
      END IF;

      IF in_terminateState IS NOT NULL THEN --选中已终结状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR T.FLAG = ''7'' OR T.TASK_STATUS = ''4''';
      END IF;

       IF in_refuseState IS NOT NULL THEN --选中已拒绝状态
        V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR T.FLAG = ''10'' OR T.TASK_STATUS = ''6''';
      END IF;

      IF in_doneState IS NOT NULL THEN --选中已完成状态
         V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR T.FLAG = ''8'' ';
      END IF;

       IF in_inactive IS NOT NULL THEN --选中未激活  add by zr
        V_STATUS_FLAG := 1 ;
         V_STATUS_COND := V_STATUS_COND || ' OR (T.FLAG = ''12'' AND (T.TASK_STATUS = ''2'' OR T.TASK_STATUS = ''1''))';
      END IF;

      IF V_STATUS_FLAG = 1 THEN
           V_WHERE := V_WHERE || V_STATUS_COND || ')';
      ELSIF V_STATUS_FLAG = 0 THEN
           V_WHERE := V_WHERE || V_STATUS_COND || ' OR T.TASK_STATUS = 1 OR T.TASK_STATUS = ''2'') and T.FLAG = ''9'' ';
      END IF;

       IF IN_DATE_B IS NOT NULL THEN
           V_TEMP_FLAG :='1';
           V_TEMP_COND := V_TEMP_COND || ' AND  TO_CHAR(' || V_TEMP_DATE || ',''YYYYMMDD'')>='||IN_DATE_B;
       END IF;
       IF IN_DATE_E IS NOT NULL THEN
           V_TEMP_FLAG :='1';
           V_TEMP_COND := V_TEMP_COND || ' AND TO_CHAR(' || V_TEMP_DATE || ',''YYYYMMDD'')<='||IN_DATE_E;
         END IF;

      IF  V_TEMP_FLAG ='1' THEN--这里是主要区别于申请查询的地方
          V_WHERE := V_WHERE || V_TEMP_COND;
      END IF;

     --改动  V_WHERE := V_WHERE ||')';
    --  V_GROUP := ' group by tt.task_id, l.loan_grade ,TASK_TFSCN_DATIM )';

      --组装语句之：记录总数
      V_SQL_COUNT := 'SELECT count(*) from (' || V_SELECT || V_FROM || V_WHERE || ' ))';
      --DBMS_OUTPUT.PUT_LINE(V_SQL_COUNT);
      OPEN V_COUNT_SET FOR V_SQL_COUNT;
      FETCH V_COUNT_SET into OUT_TOTALNUM;

      --组装语句之：记录未处理任务总数（改动）
       V_SQL_UNDOCOUNT := 'SELECT count(*) from (' || V_SELECT || V_FROM || V_WHERE || ' AND T.TASK_STATUS = ''1''  ))';
       --DBMS_OUTPUT.PUT_LINE(V_SQL_COUNT);
        OPEN V_UNDOCOUNT_SET FOR V_SQL_UNDOCOUNT;
         FETCH V_UNDOCOUNT_SET into OUT_UNDONUM;

      --组装语句之：记录处理中任务总数（改动）
       V_SQL_INDOCOUNT := 'SELECT count(*) from (' || V_SELECT || V_FROM || V_WHERE || ' AND T.TASK_STATUS = ''2''  ))';
       --DBMS_OUTPUT.PUT_LINE(V_SQL_COUNT);
        OPEN V_INDOCOUNT_SET FOR V_SQL_INDOCOUNT;
         FETCH V_INDOCOUNT_SET into OUT_INDONUM;

      --翻页结束

IF IN_PRI_TYPE IS NOT NULL AND IN_PRI_TYPE='1' THEN
      IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='2'THEN
           V_ORDER := ' ORDER BY  PRI_TYPE desc, UPGRADE_FLAG desc,URGENT_FLAG desc,CREDITGRADE asc,loan_grade desc,ANTIFRAUD_FLAG desc,  TASKDATIM '||IN_ORDER_WAY_TYPE;
         ELSIF IN_ORDER_TYPE = '1' THEN
           V_ORDER := ' ORDER BY PRI_TYPE desc,UPGRADE_FLAG desc, URGENT_FLAG desc,CREDITGRADE asc,loan_grade desc, ANTIFRAUD_FLAG desc,  pos_rcv_datim '||IN_ORDER_WAY_TYPE;
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_ORDER := ' ORDER BY  PRI_TYPE desc,UPGRADE_FLAG desc, URGENT_FLAG desc,CREDITGRADE asc,loan_grade desc,ANTIFRAUD_FLAG desc, pos_rcv_datim asc ';
      END IF;
ELSE 
      IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='2'THEN
           V_ORDER := ' ORDER BY DY_FLAG asc,URGENT_FLAG desc, loan_grade desc, TASKDATIM '||IN_ORDER_WAY_TYPE;
         ELSIF IN_ORDER_TYPE = '1' THEN
           V_ORDER := ' ORDER BY DY_FLAG asc, URGENT_FLAG desc, loan_grade desc, pos_rcv_datim '||IN_ORDER_WAY_TYPE;
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_ORDER := ' ORDER BY DY_FLAG asc,URGENT_FLAG desc, loan_grade desc, pos_rcv_datim asc ';
      END IF;
END IF;

      V_WHERE_PAGE := ' WHERE 1=1';
      IF IN_QRYBEGINPOS IS NOT NULL THEN
         V_WHERE_PAGE := V_WHERE_PAGE || ' AND  RN >= '|| IN_QRYBEGINPOS;
         IF IN_QRYNUM IS NOT NULL THEN
            V_WHERE_PAGE := V_WHERE_PAGE || ' AND RN <' || TO_CHAR(IN_QRYBEGINPOS + IN_QRYNUM,'999999999');
         END IF;
      END IF;


      --生成 生成临时表的语句
      V_SQL_ALL := V_SELECT || V_FROM || V_WHERE || ')' || V_GROUP || V_ORDER;--改动
      V_SQL_PAGE := 'select task_id from (' || V_SQL_ALL || ')' || V_WHERE_PAGE ;
      V_create_table := 'insert into query_task_id (task_id ) '|| V_SQL_PAGE ;
     -- DBMS_OUTPUT.PUT_LINE(V_create_table);
      --执行 生成临时表的语句
      execute immediate V_create_table;

------------------------------------------------------
      --加入需要显示的字段
      V_SELECT_PAGE := '
                  SELECT
                  TASK_ID,
                  WORK_ID,
                  TASK_USERID,
                  PRI_ID,
                  PRI_TYPE,
                  smt_service_flag,
                  URGENT_FLAG,
                  DY_FLAG,
                  CONDITION_FLAG,
                  TTASK_STATUS,
                  TFLAG,
                  LOAN_ID,
                  LOAN_GRADE,
                  APPLICATION_NO,
                  NEW_IMG_FLAG,
                  CSTMTYPE_ID,
                  LOANDATIM,
                  TASKDATIM,
                  RCVDATIM,
                  TASKENDDATIM,
                  LSTRCDATE,
                  BORROWER_NAME,
                  SALES_NAME,
                  BRAND_NAME,
                  DISTRIBUTOR_NAME,
                  DEALER_NAME,
                  CLEARING_CDE,
                  CITY_NAME,
                  WORKER_NAME,
                  WORKER_NO,
                  POSITION_NAME,
                  POSITION_STATUS,
                  TASK_STATUS,
                  ICOMMENT,
                   OCOMMENT,
                   CO_LOAN_FLAG,
                   CO_BANK_NAME,
                   ANTIFRAUD_FLAG,
                   UPGRADE_FLAG,
		              ISUSED,
                  ISPRE,
                  PRE_RESULT,
                  UW,
                  CREDITGRADE_TEXT,
                  BD_CHECK,
                  FUNDING_DTE
          from ( SELECT
                  distinct( T.TASK_ID),
                   T.WORK_ID,
                   T.USERID TASK_USERID,
                   T.PRI_ID,
                   T.PRI_TYPE,
                   T.smt_service_flag,
                   T.URGENT_FLAG,
                   T.DY_FLAG,
                   T.CONDITION_FLAG,
                   T.TASK_STATUS TTASK_STATUS,
                   T.FLAG  TFLAG,
                   L.LOAN_ID,
                   L.LOAN_GRADE,
                   L.APPLICATION_NO,
                   L.NEW_IMG_FLAG,
                   L.CSTMTYPE_ID,
                   L.LOAN_DATIM LOANDATIM,
                   L.CO_LOAN_FLAG,
                   L.CO_BANK_NAME,
                   T.ANTIFRAUD_FLAG,
                   pd4.pos_rcv_datim TASKDATIM,

                   case when PD.pos_rcv_datim is null
                        then ( case when PD3.num <> 0 then
                               PD2.pos_rcv_datim
                               else PD4.pos_rcv_datim end )
                        else  PD.pos_rcv_datim
                    end as  RCVDATIM,
                   T.task_end_datim TASKENDDATIM,
                   pd5.lstrcdate LSTRCDATE,
                   L.BORROWER_NAME,
                   L.SALES_NAME,
                   B.BRAND_NAME,
                   DT.DISTRIBUTOR_NAME,
                   D.DEALER_NAME,
                   D.CLEARING_CDE,
                   C.CITY_NAME,
                   U.NAME WORKER_NAME,
                   U.USERNO WORKER_NO,
                   P.POSITION_NAME,
                   PS.WORKITEM_STATUS POSITION_STATUS,
                   PS.POSITION_TASK_STATUS TASK_STATUS,
                   TIC.TASK_COMMENT ICOMMENT,
                   TOC.TASK_COMMENT OCOMMENT,
		               L.UPGRADE_FLAG,
                   L.ISUSED,
                   L.ISPRE,
                   L.PRE_RESULT,
                   L.UW,
                   L.CREDITGRADE_TEXT,
                   D.BD_CHECK,
                   L.CREDITGRADE,
                   AI.FUNDING_DTE
          FROM query_task_id tempT,
                       TASK T,
                       ETP_USER U,
                       POSITION P,
                       (
                           select tt.task_id  TASK_ID,
                             max(tt.pos_submit_datim) pos_rcv_datim
                          from task_track tt,query_task_id tmp1
                          where tt.action_type <> ''3''
                          and tmp1.task_id=tt.task_id
                          group by tt.task_id
                       ) pd,
                       (
                          select tt.task_id,
                                 tt.position_id,
                                 max(tt.pos_rcv_datim) pos_rcv_datim
                          from task_track tt,query_task_id tmp1
                          where tt.task_id=tmp1.task_id
                          group by tt.task_id, tt.position_id
                       ) pd2,
                       ( select count(*) as num ,tt.task_id
                        from task_track tt,query_task_id tmp1
                        where
                            tt.action_type<>''3'' and  tt.action_type is not null
                            and tt.task_id=tmp1.task_id
                            group by tt.task_id
                          )pd3,
                        (
                          select tt.task_id,
                                 min(tt.pos_rcv_datim) pos_rcv_datim
                          from task_track tt,query_task_id tmp1
                          where tt.task_id=tmp1.task_id
                          group by tt.task_id
                         ) pd4,

                         (select tt.task_id,
                               max(tt.pos_rcv_datim) lstrcdate
                            from task_track tt,query_task_id tmp1
                            where tt.position_id = ''7''
                            and tt.action_type = ''1''
                            and tt.task_id=tmp1.task_id
                         group by tt.task_id) pd5,

                       POS_TASK_STAT PS,
                       (
                          select MAX(IMC.COMMENT_ID) CID,
                             T.TASK_ID
                          from comments IMC,
                             TASK T,query_task_id t1
                          WHERE (IMC.COMMENT_TYPE = ''O'' or IMC.COMMENT_TYPE = ''SO'')
                             AND IMC.TASK_ID = T.TASK_ID(+)
                             and t.task_id=t1.task_id
                             and IMC.display=''1''
                          GROUP BY T.TASK_ID
                       ) MOC,
                       (
                          select MAX(IMC.COMMENT_ID) CID,
                             T.TASK_ID
                          from comments IMC,
                             TASK T,query_task_id t1
                          WHERE (IMC.COMMENT_TYPE = ''I'' or IMC.COMMENT_TYPE = ''SI'')
                             AND IMC.TASK_ID = T.TASK_ID(+)
                             and t.task_id=t1.task_id
                             and IMC.display=''1''
                          GROUP BY T.TASK_ID
                       ) MIC,          
                       COMMENTS TOC,
                       COMMENTS TIC,
                       LOAN L,
                       DEALER_LOAN_MAP DLM,
                       DEALER D,
                       CITY C,
                       REGION R,
                       BRAND_DEALER_MAP BDM,
                       BRAND B,
                       DISTRIBUTOR_DEALER_MAP DDM,
                       DISTRIBUTOR DT,
                       APPLICATION_INFO AI
       WHERE tempT.task_id = t.task_id
                         and T.LOAN_ID = L.LOAN_ID
                         AND AI.APPLICATION_NUMBER = L.APPLICATION_NO
                         AND T.USERID = U.USERID(+)
                         AND T.POSITION_ID = PS.POSITION_ID(+)
                         AND T.POSITION_ID = P.POSITION_ID
                         AND T.TASK_ID = PD.TASK_ID(+)
                          AND T.POSITION_ID = PD2.POSITION_ID
                         AND T.TASK_ID = PD2.TASK_ID
                         AND T.TASK_ID = PD3.TASK_ID(+)
                         AND T.TASK_ID = PD4.TASK_ID
                         AND T.TASK_ID = PD5.TASK_ID(+)
                         AND T.TASK_STATUS = PS.WORKITEM_STATUS_ID(+)
                         AND T.TASK_ID = MOC.TASK_ID(+)
                         AND T.TASK_ID = MIC.TASK_ID(+)
                         AND MOC.CID = TOC.COMMENT_ID(+)
                         AND MIC.CID = TIC.COMMENT_ID(+)
                         AND L.LOAN_ID = DLM.LOAN_ID(+)
                         AND DLM.DEALER_ID = D.DEALER_ID(+)
                         AND D.CITY_ID = C.CITY_ID(+)
                         AND D.REGION_ID = R.REGION_ID(+)
                         AND D.DEALER_ID = BDM.DEALER_ID(+)
                         AND BDM.BRAND_ID = B.BRAND_ID(+)
                         AND D.DEALER_ID = DDM.DEALER_ID(+)
                         AND DDM.DISTRIBUTOR_ID = DT.DISTRIBUTOR_ID(+)
                     )
       ';
------------------------------------------------------
IF IN_PRI_TYPE IS NOT NULL AND IN_PRI_TYPE='1' THEN
     IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='1'THEN
           V_ORDER_PAGE :='order by PRI_TYPE desc,UPGRADE_FLAG desc, URGENT_FLAG desc,CREDITGRADE asc, LOAN_GRADE desc,ANTIFRAUD_FLAG desc, RCVDATIM '||IN_ORDER_WAY_TYPE||' , APPLICATION_NO desc';
         ELSIF IN_ORDER_TYPE = '2' THEN
           V_ORDER_PAGE :='order by PRI_TYPE desc,UPGRADE_FLAG desc, URGENT_FLAG desc,CREDITGRADE asc, LOAN_GRADE desc,ANTIFRAUD_FLAG desc,   TASKDATIM '||IN_ORDER_WAY_TYPE||' , APPLICATION_NO desc';
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_ORDER_PAGE :='order by PRI_TYPE desc,UPGRADE_FLAG desc,URGENT_FLAG desc,CREDITGRADE asc, LOAN_GRADE desc, ANTIFRAUD_FLAG desc, RCVDATIM  asc , APPLICATION_NO desc';
       END IF;
ELSE 
      IF IN_ORDER_WAY_TYPE IS NOT NULL THEN
         IF IN_ORDER_TYPE='1'THEN
           V_ORDER_PAGE :='order by DY_FLAG asc,URGENT_FLAG desc, LOAN_GRADE desc,RCVDATIM '||IN_ORDER_WAY_TYPE||' , APPLICATION_NO desc';
         ELSIF IN_ORDER_TYPE = '2' THEN
           V_ORDER_PAGE :='order by DY_FLAG asc,URGENT_FLAG desc, LOAN_GRADE desc,TASKDATIM '||IN_ORDER_WAY_TYPE||' , APPLICATION_NO desc';
         END IF;
      ELSIF IN_ORDER_WAY_TYPE IS  NULL THEN
            V_ORDER_PAGE :='order by DY_FLAG asc,URGENT_FLAG desc, LOAN_GRADE desc,RCVDATIM  asc , APPLICATION_NO desc';
       END IF; 


END IF;

    V_SELECT_PAGE := V_SELECT_PAGE|| V_ORDER_PAGE;

      --组装语句之：分页结果集
      OPEN OUT_TASKCUR FOR V_SELECT_PAGE;

      --获得辅助列表
      OPEN OUT_BRANDCUR FOR--查询品牌记录集
        SELECT BRAND_ID,
             BRAND_NAME
        FROM BRAND
        where status = '0';
      OPEN OUT_REGIONCUR FOR--查询大区记录集
        SELECT REGION_ID,
             REGION_NAME
        FROM REGION
        where status= '0';
      OPEN OUT_DISTRIBUTORCUR FOR--查询承销商记录集
        SELECT DISTRIBUTOR_ID,
             DISTRIBUTOR_NAME
        FROM DISTRIBUTOR
        where status = '0';
      OPEN OUT_POSITIONCUR FOR--查询岗位记录集
        SELECT POSITION_ID,
             POSITION_NAME
        FROM POSITION;
      OPEN OUT_BANKCUR FOR  --查询联贷银行名称记录集 lzm
        SELECT distinct(CO_BANK_NAME)
        FROM LOAN;
   EXCEPTION
    WHEN OTHERS THEN
      IF OUT_TASKCUR%ISOPEN THEN
        CLOSE OUT_TASKCUR;
      END IF;
      IF OUT_BRANDCUR%ISOPEN THEN
        CLOSE OUT_BRANDCUR;
      END IF;
      IF OUT_REGIONCUR%ISOPEN THEN
        CLOSE OUT_REGIONCUR;
      END IF;
      IF OUT_DISTRIBUTORCUR%ISOPEN THEN
        CLOSE OUT_DISTRIBUTORCUR;
      END IF;
      IF OUT_POSITIONCUR%ISOPEN THEN
        CLOSE OUT_POSITIONCUR;
      END IF;
       IF OUT_BANKCUR%ISOPEN THEN
        CLOSE  OUT_BANKCUR;
      END IF;
      ROLLBACK;
      OUT_RETCODE := '-1';
      --DBMS_OUTPUT.PUT_LINE(SQLERRM);
      OUT_MESG := SQLERRM;
      RETURN;
  END PROC_QUERYTASK;
  
  
  
  
  delete from TDPOSTS_FILE_INFO t
    where t.file_id in (select substr(inlist,
              instr(inlist, ',', 1, level) + 1,
              instr(inlist, ',', 1, level + 1) -
              instr(inlist, ',', 1, level) - 1) as value_str
  from (select ',' || i_fileId || ',' as inlist from dual)
connect by level <=
           length(i_fileId) - length(replace(i_fileId, ',', '')) + 1);