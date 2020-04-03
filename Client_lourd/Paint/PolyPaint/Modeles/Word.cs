using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace PolyPaint.Modeles
{

    class Word : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public string suggestedWord = "";

      
            public string SuggestedWord
            {

                get { return suggestedWord; }
                set
                {
                    if (value != suggestedWord)
                    {
                        suggestedWord = value;
                        ProprieteModifiee();
                }
                }
            }
        

        protected void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}