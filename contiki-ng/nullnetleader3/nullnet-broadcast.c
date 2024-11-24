/*
 * Copyright (c) 2017, RISE SICS.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of the Contiki operating system.
 *
 */

/**
 * \file
 *         NullNet broadcast example
 * \author
*         Simon Duquennoy <simon.duquennoy@ri.se>
 *
 */
#include <stdlib.h>
#include "contiki.h"
#include "net/netstack.h"
#include "net/nullnet/nullnet.h"
#include <string.h>
#include <stdio.h> /* For printf() */
#include "sys/node-id.h"
/* Log configuration */
#include "sys/log.h"
#include "dev/leds.h"
#include "sys/node-id.h"

#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_INFO

/* Configuration */
#define SEND_INTERVAL (8 * CLOCK_SECOND)

#define CONTROL_INTERVAL (24 * CLOCK_SECOND)

#if MAC_CONF_WITH_TSCH
#include "net/mac/tsch/tsch.h"
static linkaddr_t coordinator_addr =  {{ 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }};
#endif /* MAC_CONF_WITH_TSCH */

  // Initialization, should only be called once.
 static unsigned leaderID =1000;
static unsigned cycleCount=0;

// define a new structure for xferring leadership beacons
typedef struct {
  uint8_t leaderID;	// the id of leader mote
  uint8_t number; // the source of leadership beacon
  //char *description;
} strLeader;


 
/*---------------------------------------------------------------------------*/
PROCESS(nullnet_example_process, "NullNet broadcast example");
AUTOSTART_PROCESSES(&nullnet_example_process);

/*---------------------------------------------------------------------------*/
void input_callback(const void *data, uint16_t len,
  const linkaddr_t *src, const linkaddr_t *dest)
{

    strLeader leader; 
    memcpy(&leader, data, sizeof( strLeader));
  uint16_t  senderID = (uint16_t)src->u8[0];
  
    LOG_INFO("Mevcut liderID %u", leaderID);
  LOG_INFO_("\n");
    LOG_INFO("Alındı %u data from %u  number= %u", leader.leaderID,senderID, leader.number);
//LOG_INFO("Açıklama %s ----------",leader.description);
    LOG_INFO_("\n");

    LOG_INFO_LLADDR(src);
    LOG_INFO_("\n");
 
if(cycleCount>20){
  leaderID=0;
  cycleCount=0;
leds_on(LEDS_RED);
        LOG_INFO("eski leader %u yeni lider ben %u gönderen %u", leaderID, node_id, senderID);

}
    
 else if(leader.leaderID<leaderID){
      leaderID=leader.leaderID;
    }
  
  if(leaderID==0){

leds_off(LEDS_GREEN);
    leaderID=node_id;
  }

  if(senderID==leaderID){
    cycleCount=0;
         LOG_INFO("lider gönderdi leader %u gönderen %u", leaderID, senderID);
     leds_on(LEDS_RED);
  }
  else{
    cycleCount++;
         LOG_INFO("gönderen lider değil leader %u  cycle = %u gönderen %u", leaderID, cycleCount, senderID);
  }
  

  if(leaderID==node_id){
     leds_on(LEDS_GREEN);
  }else
  {
     leds_off(LEDS_GREEN);
  }

     LOG_INFO("Son liderID %u", leaderID);
  LOG_INFO_("\n");
}

/*---------------------------------------------------------------------------*/
PROCESS_THREAD(nullnet_example_process, ev, data)
{
  static struct etimer periodic_timer;

  PROCESS_BEGIN();

#if MAC_CONF_WITH_TSCH
  tsch_set_coordinator(linkaddr_cmp(&coordinator_addr, &linkaddr_node_addr));
#endif /* MAC_CONF_WITH_TSCH */
  leaderID=node_id;

  strLeader leader={leaderID,24};

//leader.description="emrullah"+node_id;
  //  LOG_INFO("Gönderiliyoren  %s to ", leader.description);
  /* Initialize NullNet */
  //nullnet_buf = (uint16_t *)&leader;
  nullnet_len = sizeof(leader);
  nullnet_set_input_callback(input_callback);

  etimer_set(&periodic_timer, SEND_INTERVAL);
 

 
  //LOG_INFO("haydi ", LOG_INFO_LLADDR());
    LOG_INFO("My Id is : %u", node_id);
  while(1) {
    PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&periodic_timer));

cycleCount++;
    if(cycleCount>20){
      cycleCount=0;
       LOG_INFO("6 defa gelmedi eski leader %u yeni lider ben %u", leader.leaderID, node_id);
        leds_off(LEDS_GREEN);
      leaderID=0;
    }
    leader.leaderID=leaderID;
    leader.number=12;
    LOG_INFO("Gönderiliyor %u lider id number %u ", leader.leaderID, leader.number);
    LOG_INFO_LLADDR(NULL);
    LOG_INFO_("\n");
    
   
  
     
    memcpy(nullnet_buf, &leader, sizeof(&leader));  
    //nullnet_len = sizeof(leader);

    NETSTACK_NETWORK.output(NULL);
    etimer_reset(&periodic_timer);
  }

  PROCESS_END();
}
/*---------------------------------------------------------------------------*/
