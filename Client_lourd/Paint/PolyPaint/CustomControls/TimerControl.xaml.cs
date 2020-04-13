using PolyPaint.VueModeles;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace PolyPaint.CustomControls
{
    /// <summary>
    /// Interaction logic for timer.xaml
    /// </summary>
    public partial class TimerControl : UserControl
    {

        public int time = 10;
        public DispatcherTimer Timer;
        public EventHandler _end;

        public TimerControl()
        {
            InitializeComponent();
            Timer = new DispatcherTimer();
            Timer.Interval = new TimeSpan(0, 0, 1);
            Timer.Tick += Timer_Tick;
            Timer.Start();
            
        }

        void Timer_Tick(object sender, EventArgs e)
        {
            if (time > 0)
            {
                {

                    if (time <= 10)
                    {

                        if (time % 2 == 0)
                        {
                            TBCountDown.Foreground = Brushes.Red;
                        }
                        else
                        {
                            TBCountDown.Foreground = Brushes.Black;
                        }
                        time--;
                        TBCountDown.Text = string.Format("0{0}:0{1}", time / 60, time % 60);
                    }
                    else
                    {
                        time--;
                        TBCountDown.Text = string.Format("0{0}:{1}", time / 60, time % 60);
                    }
                }

            }

            else
            {
                Timer.Stop();
                _end?.Invoke(sender, e);
            

                //    string vm = ((BaseViewModel)(DataContext)).GetCurrentViewModelName();
                //    if (vm == "GuessingViewModel")
                //{
                //    ((GuessingViewModel)(DataContext)).assignDrawingView();
                //}
                //    if (vm == "FreeForAllViewModel")
                //{
                //    ((FreeForAllViewModel)(DataContext)).assignGuessingView();
                //}
            }
            
            }
 



    }

   
}
