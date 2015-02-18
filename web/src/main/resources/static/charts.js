/**
 * Created by zac on 17/02/15.
 */
var Charts = function () {
    var
      lastValuesToShow = 60,
      messagesPerSecond = null,
      messagesPerSecondData = ["Requests"],
      volumePerSecond = null,
      volumePerSecondData = ["Volume"],

      initialize = function () {
        for (var i = 0; i < lastValuesToShow; i++) {
          messagesPerSecondData.push(0);
          volumePerSecondData.push(0);
        }

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
        //messagesPerSecond.axis.range({max: {y: 10000}});

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
    if (messagesPerSecondData.length > lastValuesToShow) {
      messagesPerSecondData = messagesPerSecondData.slice(0, lastValuesToShow);
    }
    messagesPerSecond.load({
      columns: [
        messagesPerSecondData
      ]
    });

    volumePerSecondData.splice(1, 0, stats.volumeBuy);
    if (volumePerSecondData.length > lastValuesToShow) {
      volumePerSecondData = volumePerSecondData.slice(0, lastValuesToShow);
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


