using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace PolyPaint.Modeles
{

    class Dessin : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public StrokeCollection traits = new StrokeCollection();

        private string dessin = "";
        public string NouveauDessin
        {
            get { return dessin; }
            set { dessin = value; ProprieteModifiee(); }
        }

        protected void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        public void EtablirNouveauDessin(string dessin) => NouveauDessin = dessin;
    }
}