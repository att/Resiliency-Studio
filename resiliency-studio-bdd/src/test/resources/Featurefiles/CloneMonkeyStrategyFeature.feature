# * BSD License
# *     
# * Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.       
# * 
# *  Redistribution and use in source and binary forms, with or without modification, are permitted
# *   provided that the following conditions are met:
# *
# *   1. Redistributions of source code must retain the above copyright notice, this list of conditions
# *      and the following disclaimer.
# *   2. Redistributions in binary form must reproduce the above copyright notice, this list of
# *      conditions and the following disclaimer in the documentation and/or other materials provided
# *      with the distribution.
# *   3. All advertising materials mentioning features or use of this software must display the
# *      following acknowledgement:  This product includes software developed by the AT&T.
# *   4. Neither the name of AT&T nor the names of its contributors may be used to endorse or
# *      promote products derived from this software without specific prior written permission.
# *
# *   THIS SOFTWARE IS PROVIDED BY AT&T INTELLECTUAL PROPERTY ''AS IS'' AND ANY EXPRESS OR
# *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# *   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
# *   SHALL AT&T INTELLECTUAL PROPERTY BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS;
# *   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
# *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
# *   DAMAGE.
# *******************************************************************************
Feature: Clone Monkey strategy Page
  As a user I want to clone a monkey strategy in the system 
 
 @CloneMonkeyStrategy
  Scenario: CloneMonkeyStrategy
    ##Given I am entering the credentials for clone monkey strategy
    Given I am on the dash board page for clone monkey strategy
    Given I am on view monkey strategies and clicking on the clone link for particular monkey "TestMonkey2"
    Then validate the monkey strategy name is ending with clone string and view the script in the basic tab
    Then validate the mandatory fields are filled with data  while loading the page
    |monkeyStrategyName|monkeyType|
    |strategy|Latency|
    Then code the script and update it and upload the new script
    Then click on the advance tab
    Then check weather generic toggle is on then check all the mandatory fields are filled
    |osType|flavor|failureCategory|failureSubCategory|
    |1	|1		|software|java|
    Then click on the clone button and validate the monkey strategy is cloned 
    Then check for the generic toggle is off ideally it should off after submitting the clone
    Then reset all the fields for a clone monkey strategy where generic toggle is on
    Then submit the monkey strategy only by filling the basic tab
    