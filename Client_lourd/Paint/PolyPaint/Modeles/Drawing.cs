using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace PolyPaint.Modeles
{

    class Drawing : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
     
        public StrokeCollection StrokesCollection = new StrokeCollection();

        protected void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}