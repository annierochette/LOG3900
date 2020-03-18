using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Utilitaires
{
    public static class SocketEvents
    {
        public static readonly string STROKE_COLLECTED = "StrokeCollected";
        public static readonly string STROKE_DRAWING = "StrokeDrawing";
        public static readonly string STROKE_COLOR = "CouleurSelectionnee";
        public static readonly string STROKE_TIP = "PointeSelectionnee";
        public static readonly string STROKE_TOOL = "OutilSelectionne";
        public static readonly string STROKE_SIZE = "TailleTrait";
    }
}
