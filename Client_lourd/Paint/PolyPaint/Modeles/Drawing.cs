using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace PolyPaint.Modeles
{

    class Drawing : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public StrokeCollection traits = new StrokeCollection();

        private string drawing = "";
        public string NewDrawing
        {
            get { return drawing; }
            set { drawing = value; ProprieteModifiee(); }
        }

        protected void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        public void DrawNewImage(string drawing) => NewDrawing = drawing;
    }
}