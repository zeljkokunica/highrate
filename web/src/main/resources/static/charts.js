/**
 * Created by zac on 17/02/15.
 */
var Charts = function () {
    var
      messagesPerSecond = null,
      messagesPerSecondData = ["Requests", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      volumePerSecond = null,
      volumePerSecondData = ["Volume", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],

      initialize = function () {
        messagesPerSecond = c3.generate({
          bindto: '#messagesPerSecond',
          data: {
            columns: [
              messagesPerSecondData
            ],
            types: {
              Requests: 'line'
            }
          },
          axis: {
            y: {
              label: {
                text: 'requests per second',
                position: 'outer-middle'
              }
            },
            x: {
              label: {
                text: "second"
              }
            }
          },
          legend: {
            show: false
          }
        });
        messagesPerSecond.axis.range({max: {y: 10000}});

        volumePerSecond = c3.generate({
          bindto: '#volumePerSecond',
          data: {
            columns: [
              volumePerSecondData
            ],
            types: {
              Requests: 'line'
            }
          },
          axis: {
            y: {
              label: {
                text: 'volume per second',
                position: 'outer-middle'
              }
            },
            x: {
              label: {
                text: "second"
              }
            }
          },
          legend: {
            show: false
          }
        });
        volumePerSecond.axis.range({min: {y: 0}});
  },
  addLastSecondStats = function (stats) {
    messagesPerSecondData.splice(1, 0, stats.count);
    if (messagesPerSecondData.length > 20) {
      messagesPerSecondData = messagesPerSecondData.slice(0, 20);
    }
    messagesPerSecond.load({
      columns: [
        messagesPerSecondData
      ]
    });

    volumePerSecondData.splice(1, 0, stats.volumeBuy);
    if (volumePerSecondData.length > 20) {
      volumePerSecondData = volumePerSecondData.slice(0, 20);
    }
    volumePerSecond.load({
      columns: [
        volumePerSecondData
      ]
    });
  };
initialize();

return {
  addLastSecondStats: addLastSecondStats
};
}


