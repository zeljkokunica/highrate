/**
 * Created by zac on 17/02/15.
 */
var BarChart = function (container, valueUnit, valueField) {
  var
    chart = null,

    initialize = function () {
      chart = c3.generate({
        title: "Test",
        bindto: container,
        data: {
          x : 'x',
          columns: [
            ['x'],
            [valueUnit]
          ],
          type: 'bar'

        },
        axis: {
          x: {
            type: 'category' // this needed to load string x value
          },
          y: {
            label: {
              text: valueUnit,
              position: 'outer-middle'
            }
          },
          y2: {
            label: {
              text: valueUnit,
              position: 'outer-middle'
            }
          }
        },
        bar: {
          width: {
            ratio: 0.5 // this makes bar width 50% of length between ticks
          }
          // or
          //width: 100 // this makes bar width 100px
        },
        legend: {
          show: false
        }
      });
    },
    processStats = function (stats) {
      var data = [['x'], [valueUnit]];
      for(var key in stats) {

        var value = stats[key][valueField];
        data[0].push(key);
        data[1].push(value);
      }
      chart.load({
        columns: data
      });
    };
  initialize();

  return {
    processStats: processStats
  };
}


