/**
 * Created by zac on 17/02/15.
 */
var LineChart = function (countContainer, valueUnit, timeUnit, valueField) {
  var
    messagesChart = null,

    initialize = function () {
      messagesChart = c3.generate({
        bindto: countContainer,
        data: {
          columns: [valueUnit],
          types: {
            Requests: 'line'
          }
        },
        axis: {
          y: {
            label: {
              text: valueUnit,
              position: 'outer-middle'
            }
          },
          x: {
            label: {
              text: timeUnit
            }
          }
        },
        legend: {
          show: false
        }
      });
    },
    processStats = function (stats) {
      var messagesPerSecondData = ["Requests"].concat(stats.map(function(item){return item[valueField]}));
      messagesChart.load({
        columns: [
          messagesPerSecondData
        ]
      });
    };
  initialize();

  return {
    processStats: processStats
  };
}


