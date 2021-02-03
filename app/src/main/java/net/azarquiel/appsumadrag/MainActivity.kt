package net.azarquiel.appsumadrag

import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnDragListener {

    private var intentos: Int = 0
    private var n1: Int = 0
    private var n2: Int = 0
    private var solucion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDigitos()
        init()
        oreja()

    }


    private fun oreja() {
        lldigitos.setOnDragListener(this)

    }

    private fun showDigitos() {
        var iv: ImageView
        var lp: LinearLayout.LayoutParams

        for (i in 0 until 10) {
            iv = ImageView(this)
            iv.setImageResource(resources.getIdentifier("n$i", "drawable", packageName))
            lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120, 1F)
            iv.layoutParams = lp
            lp.weight = 1F
            iv.tag = i
            iv.setOnTouchListener(this)
            lldigitos.addView(iv)
        }

    }

    private fun init() {
        n1 = (Math.random()*9999).toInt()+1
        showNumero(n1, lln1)
        n2 = (Math.random()*9999).toInt()+1
        showNumero(n2, lln2)
        solucion = n1 + n2
        showSolucion(solucion, llsol)

    }

    private fun showNumero(n: Int, linear: LinearLayout) {
        val ntxt = "$n"
        var id :Int
        var iv : ImageView
        var lp: LinearLayout.LayoutParams
        for (i in 0 until ntxt.length ) {
            id = resources.getIdentifier("n${ntxt[i]}", "drawable", packageName)
            iv = ImageView(this)
            iv.setImageResource(id)
            lp = LinearLayout.LayoutParams(200, 200)
            lp.weight = 1F
            iv.layoutParams = lp
            linear.addView(iv)

        }

    }


    private fun showSolucion(n: Int, linear: LinearLayout) {
        val ntxt = "$n"
        var id :Int
        intentos = ntxt.length
        var iv : ImageView
        var lp: LinearLayout.LayoutParams
        var linearChico:LinearLayout
        for (i in 0 until ntxt.length ) {
            id = resources.getIdentifier("n${ntxt[i]}", "drawable", packageName)
            iv = ImageView(this)
            iv.setImageResource(id)
            iv.tag = ntxt[i]
            iv.visibility = View.INVISIBLE
            lp = LinearLayout.LayoutParams(200, 200)
            lp.weight = 1F
            iv.layoutParams = lp
            linearChico = LinearLayout(this)
            linearChico.setOnDragListener(this)
            linearChico.addView(iv)
            linear.addView(linearChico)
        }



    }

    override fun onTouch(v: View, e: MotionEvent): Boolean {
        return if (e.action == MotionEvent.ACTION_DOWN) {
            val shadowBuilder = DragShadowBuilder(v)
            v.startDrag(null, shadowBuilder, v, 0)
            // v.setVisibility(View.INVISIBLE);
            true
        } else {
            false
        }
    }

    // En DROP (al soltar)
    // 1 Encontrar la view implicada (imagen soltada en este caso). Su padre es el contenedor origen
    // 2 Encontrar contenedor destino
    // 3 Quitamos la imagen del origen
    // 4 La añadimos al destino
    override fun onDrag(v: View, e: DragEvent): Boolean {
        if (e.action == DragEvent.ACTION_DROP) {
            val imagenSoltada = e.localState as ImageView
            val contenedorDestino = v as LinearLayout
            val imagensol = contenedorDestino.getChildAt(0)as ImageView
            if (imagenSoltada.tag.toString() == imagensol.tag.toString()){
                imagensol.visibility = View.VISIBLE
                intentos--
                if (intentos==0)
                    gameover()
            }
            // Si el contenedor destini tuviera algo se accederia:
            // View imagendestino = (View) contenedorDestino.getChildAt(0);

            
        }
        return true
    }

    private fun gameover() {
        alert ("Restart?") {
            title = "Felicidades"
            positiveButton("Yes"){
                val intent = intent
                finish()
                startActivity(intent)
                }
            negativeButton("No"){quitarOrejas()}
        } .show ()

    }

    private fun quitarOrejas() {
        for (i in 0 until lldigitos.childCount)
            lldigitos.getChildAt(i).setOnTouchListener(null)
        for (i in 0 until llsol.childCount)
            llsol.getChildAt(i).setOnTouchListener(null)
    }

}