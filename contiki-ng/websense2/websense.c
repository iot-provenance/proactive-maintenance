
#include "contiki.h"
#include "rpl.h"
#include "httpd-simple.h"
#include "dev/leds.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "Web Sense"
#define LOG_LEVEL LOG_LEVEL_INFO

 static int batteryLevel =1800;

bool prefix(const char *pre, const char *str)
{
    return strncmp(pre, str, strlen(pre)) == 0;
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(generate_routes(struct httpd_state *s))
{
  char buff[40];

batteryLevel=batteryLevel-20;

if(batteryLevel>1700){
   leds_on(LEDS_GREEN);
  leds_off(LEDS_RED);
}
else if(batteryLevel>500){
   leds_off(LEDS_GREEN);
   leds_on(LEDS_RED);
}
else{
   leds_on(LEDS_RED);
   leds_off(LEDS_GREEN);
}


printf("url %s\n", s->filename);



  PSOCK_BEGIN(&s->sout);

if(prefix("/r",s->filename)){
  batteryLevel = 1800;
  printf("replacing/resetting battery to 1000\n");
    sprintf(buff,"{\"act\":1, \"success\":1, \"battery\":%u}", batteryLevel);
}else{

  int temperature = rand() % 25 +15;
  int cpuUsage =  rand() % 100 +1;
  int memoryUsage = rand() % 100 +1;
  int gas =  rand() % 100;

  if(gas<23){
    gas=0;
  }


  if(cpuUsage>70){
      LOG_INFO("cpu usage over 70");
  }
    if(memoryUsage>70){
      LOG_INFO("memory usage over 70");
  }
    if(temperature>50){
      LOG_INFO("cpu over 50");
  }
    if(gas>0){
      LOG_INFO("gas detected");
  }


  LOG_INFO("cpu:%u memory: %u battery: %u temp:%u gas: %u ",cpuUsage, memoryUsage,batteryLevel, temperature,gas);

  sprintf(buff,"{\"t\":%u, \"b\":%u, \"c\":%u, \"m\":%u, \"g\":%u}",temperature, batteryLevel, cpuUsage,memoryUsage,gas);
}
  printf("send json to requester\n");
  
  SEND_STRING(&s->sout, buff);

  PSOCK_END(&s->sout);
}
/*---------------------------------------------------------------------------*/
PROCESS(webserver_nogui_process, "Web Sense server");
PROCESS_THREAD(webserver_nogui_process, ev, data)
{
  PROCESS_BEGIN();

  httpd_init();

  while(1) {
    PROCESS_WAIT_EVENT_UNTIL(ev == tcpip_event);
    httpd_appcall(data);
  }

  PROCESS_END();
}
/*---------------------------------------------------------------------------*/
httpd_simple_script_t
httpd_simple_get_script(const char *name)
{
  return generate_routes;
}
/*---------------------------------------------------------------------------*/
/* Declare and auto-start this file's process */
PROCESS(web_sense, "Web Sense");
AUTOSTART_PROCESSES(&web_sense);

/*---------------------------------------------------------------------------*/
PROCESS_THREAD(web_sense, ev, data)
{
  PROCESS_BEGIN();

  PROCESS_NAME(webserver_nogui_process);
  process_start(&webserver_nogui_process, NULL);

  LOG_INFO("Web Sense started\n");

  PROCESS_END();
}

