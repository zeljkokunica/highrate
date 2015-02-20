$(document).ready(function() {
  var
    stompClient = null,
    chartSeconds = new LineChart("#messagesPerSecond", "requests", "second", "count"),
    chartMinutes = new LineChart("#messagesPerMinute", "requests", "minute", "count"),
    chartCurrencies = new BarChart("#messagesPerCurrency", "requests", "count"),
    chartCountries = new BarChart("#messagesPerCountry", "requests", "count");

  function setConnected(connected) {
  }

  function connect() {
    var socket = new SockJS('/action');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      setConnected(true);
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/stats', function (message) {
        var parsedStats = JSON.parse(message.body);
        $("#requests").html(parsedStats.lifetime.count);
        $("#volume").html(parsedStats.lifetime.volumeBuy.toFixed(2));
        chartSeconds.processStats(parsedStats.seconds);
        chartMinutes.processStats(parsedStats.minutes);
        chartCurrencies.processStats(parsedStats.perCurrency)
        chartCountries.processStats(parsedStats.perCountry)
      });
    });
  }

  function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
  }

  connect();
});