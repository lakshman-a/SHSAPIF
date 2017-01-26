<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

  <xsl:template match="/">
    <HTML>
    <HEAD>
    <LINK REL="StyleSheet" HREF="Stylesheet.css" TYPE="text/css" media="screen"/>
    <SCRIPT language="javascript" type="text/javascript">
			//Toggle Vertical Menu
			
			function toggleMenu(id,b) 
			{
							
				if (document.getElementById) 
				{
					var e = document.getElementById(id);
					
					var b = document.getElementById(b);				
				
					if (e) 
					{
						if (e.style.display != "block") 
						{
							e.style.display = "block";
							b.src='_icons/check-min.jpg';
						}
						else 
						{
							e.style.display = "none";
							b.src='_icons/check-plus.jpg';
						}
					}  
				}
			}
			function viewall()
			{
				var e = document.all.tags("DIV");
				var b = document.all.tags("img");
				
				for (var i = 0; i &lt; e.length; i++)
				{
					if (e[i].style.display == "none")
					{
						e[i].style.display = "block";
					}
				}
				for (var i = 0; i &lt; b.length; i++)
				{	
					if (b[i].id != "m1" &amp;&amp; b[i].id != "m2")
					{
						if (b[i].src.substring(b[i].src.lastIndexOf("_"), b[i].src.length) == "_icons/check-plus.jpg")
						{
							b[i].src = "_icons/check-min.jpg";
						}
					}
				}
			}
			function collapseall()
			{
				var e = document.all.tags("DIV");
				var b = document.all.tags("img");
				
				for (var i = 0; i &lt; e.length; i++)
				{
					if(e[i].id != "Batch" &amp;&amp; e[i].id != "Test" &amp;&amp; e[i].id != "AppComponent" &amp;&amp; e[i].id != "Event" &amp;&amp; e[i].id != "AppEvent")
					{
						if (e[i].style.display == "block")
						{
							e[i].style.display = "none";
						}
					}
				}
				for (var i = 0; i &lt; b.length; i++)
				{
					if (b[i].id != "m1" &amp;&amp; b[i].id != "m2")
					{
						if (b[i].src.substring(b[i].src.lastIndexOf("_"), b[i].src.length) == "_icons/check-min.jpg")
							{
								b[i].src = "_icons/check-plus.jpg";
							}
					}
				}
			}
		</SCRIPT>
    </HEAD>
    <BODY>
    <table cellpadding="0" cellspacing="0" border="0" class="header" >
      <tr>
        <td><img border="0" src="_icons/Plintron_Logo.png" alt="" /></td>
        <td><h2>BSS Test Automation Report</h2>
	<p><b>CPOS - Automation</b></p></td>
      </tr>
    </table>
    <xsl:apply-templates select="TestReport/TestSuite">
    </xsl:apply-templates>
    </BODY>
    </HTML>
  </xsl:template>
  <xsl:template match="TestReport/TestSuite">
    <div class="details" >
      <div class="details-lft"></div>
      <div class="details-rht">
        <p>Execution Summary</p>
	<table cellpadding="0" cellspacing="0" border="0" class="details-tab">
          <tr>
            <td class="bluecol">Execution Started</td>
            <td class="whitecol"><xsl:value-of select="@ExecutionStarted"></xsl:value-of></td>
            <td class="bluecol">Execution Ended</td>
            <td class="whitecol"><xsl:value-of select="@ExecutionEnded"></xsl:value-of></td>
            <td class="bluecol">Operating System</td>
            <td class="whitecol"><xsl:value-of select="@OperatingSystem"></xsl:value-of></td>
          </tr>
          <tr>
            <td class="bluecol">Scripts Executed</td>
            <td class="whitecol"><xsl:value-of select="count(TestCase)"></xsl:value-of></td>
            <td class="bluecol">Passed</td>
            <td class="whitecol"><xsl:value-of select="count(TestCase[@Status='1'])"></xsl:value-of></td>
            <td class="bluecol">Failed</td>
            <td class="whitecol"><xsl:value-of select="count(TestCase[@Status='3'])"></xsl:value-of></td>
          </tr>
        </table>
      </div>
    </div>
    <div style="margin:0px 4%; width:990px; height:20px; clear:both;">
      <ul class="icn-box">
        <li>          
          <img src="_icons/check-plus.jpg" onClick="viewall()" id="m1" style="margin-right:5px"></img>          
          <xsl:text></xsl:text><a href="#"  onClick="viewall()">View All</a><xsl:text></xsl:text></li>
        <li>          
          <img src="_icons/check-min.jpg" onClick="collapseall()" id="m2" style="margin-right:5px"></img>          
          <xsl:text></xsl:text><a href="#" onClick="collapseall()">Collapse All</a></li>
      </ul>
    </div>
    <DIV id="Batch" Style="POSITION: relative; display:block; clear:both;" class="TABLEHEAD">
      <!-- DIV To Display Test Scenario -->
      <TABLE cellSpacing="1" cellPadding="1" Class="Batch" onClick="toggleMenu('DIV{position()}.0', 'm{position()}.0')">
        <TR>
          <TD class="message"><img id="m{position()}.0" src="_icons/check-plus.jpg" border="0" style="margin-right:5px" ></img><xsl:text></xsl:text><xsl:value-of select="@Description"></xsl:value-of></TD>
          <xsl:if test="count(TestCase[@Status='1'])>0 and count(TestCase[@Status='3'])=0">
            <TD class="head-passed">Result: Passed</TD>
          </xsl:if>
          <xsl:if test="count(TestCase[@Status='3'])>0">
            <TD class="head-failed">Result: Failed</TD>
          </xsl:if>
        </TR>
      </TABLE>
    </DIV>
    <DIV id="DIV{position()}.0" Style="display:none;margin:0px 5%;">
      <!-- DIV To Collapse Test Scenario -->
      <xsl:apply-templates select="TestCase">
        <xsl:with-param name="BatchPosition" select="position()"/></xsl:apply-templates>
    </DIV>
    <BR>
    </BR>
  </xsl:template>
  <xsl:template match="TestCase">
    <xsl:param name="BatchPosition"/>
    <DIV id="TestCase" style="POSITION: relative; display:block;">
      <!-- DIV To Display Test Case -->
      <TABLE cellSpacing="1" cellPadding="1" Class="TestCase" onClick="toggleMenu('DIV{concat($BatchPosition,'.',position())}', 'm{concat($BatchPosition,'.',position())}')">
        <TR>
          <TD class="message"><img id="m{concat($BatchPosition,'.',position())}" src="_icons\check-plus.jpg" border="0" style="margin-right:5px" ></img> <xsl:text></xsl:text><xsl:value-of select="@Description"></xsl:value-of></TD>
          <xsl:if test="(@Status='1')">
            <TD class="passed">Result: Passed</TD>
          </xsl:if>
          <xsl:if test="(@Status='3')">
            <TD class="failed">Result: Failed</TD>
          </xsl:if>
        </TR>
      </TABLE>
    </DIV>
    <DIV id="DIV{concat($BatchPosition,'.',position())}" Style="display:none">
      <!-- DIV To Collapse Test Case -->
      <xsl:apply-templates select="AppComponent|ABP">
        <xsl:with-param name="TestCasePosition" select="concat($BatchPosition,'.',position())"/></xsl:apply-templates>
    </DIV>
  </xsl:template>
  <xsl:template match="AppComponent">
    <xsl:param name="TestCasePosition"/>
    <DIV id="AppComponent" Style="LEFT: 10px; POSITION: relative; text-align:left; display:block"  >
      <!-- DIV To Display BPC -->
      <TABLE cellSpacing="1" cellPadding="1" Class="AppComponent" onClick="toggleMenu('DIV{concat($TestCasePosition,'.',position())}', 'm{concat($TestCasePosition,'.',position())}')">
        <TR>
          <TD class="message"><img id="m{concat($TestCasePosition,'.',position())}" src="_icons\check-plus.jpg" border="0" style="margin-right:5px" ></img><xsl:text></xsl:text><xsl:value-of select="@Description"></xsl:value-of></TD>
          <xsl:if test="count(Event[@Status='1'])>0 and count(Event[@Status='3'])=0">
            <TD class="passed">Result: Passed</TD>
          </xsl:if>
          <xsl:if test="count(Event[@Status='3'])>0">
            <TD class="failed">Result: Failed</TD>
          </xsl:if>
        </TR>
      </TABLE>
    </DIV>
    <DIV ID="DIV{concat($TestCasePosition,'.',position())}" Style="display:none">
      <!-- DIV To Collapse AppComponent -->
      <xsl:apply-templates select="Event"></xsl:apply-templates>
      <xsl:apply-templates select="AppEvent"></xsl:apply-templates>
    </DIV>
  </xsl:template>
  <xsl:template match="Event">
    <DIV id="Event" Style="LEFT: 25px;POSITION: relative; text-align:left; display:block">
      <!-- DIV To Display Event -->
      <TABLE cellSpacing="1" cellPadding="1" Class="MESSAGES">
        <xsl:element name="TR">
          <xsl:if test="position()mod 2">
            <xsl:attribute name="class">M1</xsl:attribute>
          </xsl:if>
          <xsl:if test="not(position()mod 2)">
            <xsl:attribute name="class">M2</xsl:attribute>
          </xsl:if>
          <xsl:element name="TD" >
            <xsl:if test="@Status='1'">
              <img src="_icons\Passed_Icon.jpg" border="0" align="middle" style="margin-right:5px">
              </img>
            </xsl:if>
            <xsl:if test="@Status='3'">
              <img src="_icons\Failed_Icon.jpg" border="0" align="middle" style="margin-right:5px">
              </img>
            </xsl:if>
            <xsl:if test="@Status='2'">
              <img src="_icons\Warning_Icon.jpg" border="0" align="middle" style="margin-right:5px" >
              </img>
            </xsl:if>
            <xsl:if test="@Status='4'">
              <img src="_icons\Information_Icon.jpg" border="0" align="middle" style="margin-right:5px">
              </img>
            </xsl:if>
            <xsl:text></xsl:text><xsl:value-of select=".">
            </xsl:value-of>
            <xsl:if test="@ErrorScreenShotPath">
              <xsl:text></xsl:text><a href="{@ErrorScreenShotPath}" target="_new">Error ScreenShot</a>
            </xsl:if>
            <xsl:if test="@ScreenShotPath">
              <xsl:text></xsl:text><a href="{@ScreenShotPath}" target="_new">ScreenShot</a>
            </xsl:if>
          </xsl:element>
        </xsl:element>
      </TABLE>
    </DIV>
  </xsl:template>
  <xsl:template match="AppEvent">
    <DIV id="Event" Style="LEFT: 25px;POSITION: relative; display:block">
      <!-- DIV To Display Event -->
      <TABLE cellSpacing="1" cellPadding="1" align="center" Class="MESSAGES">
        <xsl:element name="TR">
          <xsl:if test="position()mod 2">
            <xsl:attribute name="class">M1</xsl:attribute>
          </xsl:if>
          <xsl:if test="not(position()mod 2)">
            <xsl:attribute name="class">M2</xsl:attribute>
          </xsl:if>
          <xsl:element name="TD" >
            <xsl:if test="@Status='1'">
              <img src="_icons\Passed_Icon.jpg" border="0" align="middle">
              </img>
            </xsl:if>
            <xsl:if test="@Status='3'">
              <img src="_icons\Failed_Icon.jpg" border="0" align="middle">
              </img>
            </xsl:if>
            <xsl:if test="@Status='2'">
              <img src="_icons\Warning_Icon.jpg" border="0" align="middle" >
              </img>
            </xsl:if>
            <xsl:if test="@Status='4'">
              <img src="_icons\Information_Icon.jpg" border="0" align="middle">
              </img>
            </xsl:if>
            <xsl:text></xsl:text><xsl:value-of select=".">
            </xsl:value-of>
            <xsl:if test="@ErrorScreenShotPath">
              <xsl:text></xsl:text><a href="{@ErrorScreenShotPath}" target="_new">Error ScreenShot</a>
            </xsl:if>
            <xsl:if test="@ScreenShotPath">
              <xsl:text></xsl:text><a href="{@ScreenShotPath}" target="_new">ScreenShot</a>
            </xsl:if>
          </xsl:element>
        </xsl:element>
      </TABLE>
    </DIV>
  </xsl:template>
</xsl:stylesheet>
