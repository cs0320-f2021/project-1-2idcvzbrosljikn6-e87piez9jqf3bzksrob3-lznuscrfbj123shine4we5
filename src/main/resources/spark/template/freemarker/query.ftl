<#assign content>
  <head>
    <link rel="stylesheet" href="main.css">
  </head>

  <div class="stars"><h1> S T A R S </h1></div>

  <div class="center">
    <p>${error}</p>
  </div>

  <div class="center">
    <p> <h3>K-NEAREST NEIGHBORS</h3>
    <form method="POST" action="/neighbors">
      <label for="text">'k x y z' OR 'k "name"'</label><br>
      <textarea name="text"></textarea><br>
      <input type="submit">
    </form>
    <table style="width:100%">
      <#list neighbors>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Coordinates</th>
        </tr>
        <#items as star>
        <tr>
          <#list star as attribute>
            <th>${attribute}</th>
          </#list>
        </tr>
        </#items>
      </#list>
      </p>
    </table>
  </div>

  <div class="center">
    <p> <h3>RADIUS SEARCH</h3>
    <form method="POST" action="/radius">
      <label for="text">'r x y z' OR 'r "name"'</label><br>
      <textarea name="text"></textarea><br>
      <input type="submit">
    </form>
    <table style="width:100%">
      <#list radius>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Coordinates</th>
        </tr>
        <#items as star>
          <tr>
            <#list star as attribute>
              <th>${attribute}</th>
            </#list>
          </tr>
        </#items>
      </#list>
      </p>
    </table>
  </div>
</#assign>
<#include "main.ftl">