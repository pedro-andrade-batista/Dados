package br.edu.ifsp.scl.ads.pdm.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import br.edu.ifsp.scl.ads.pdm.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>
    private var numeroDados: Int = 1
    private var numeroFaces: Int = 6
    private var flagToShowDice = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        geradorRandomico = Random(System.currentTimeMillis())

        activityMainBinding.jogarDadoBt.setOnClickListener {

            val resultado: Int = geradorRandomico.nextInt(1..numeroFaces)

            flagToShowDice = !(resultado > 6)

            showDices(resultado)
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                if(result.data != null){
                    val configuracao: Configuracao? =  result.data?.getParcelableExtra(Intent.EXTRA_USER)
                    if (configuracao != null) {
                        numeroDados = configuracao.numeroDados
                        numeroFaces = configuracao.numeroFaces
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMi){
            val settingsIntent =Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

    fun showDices(resultado: Int){
        var resultado2: Int
        val nomeImagem = "dice_$resultado"
        var nomeImagem2 = ""
        var display = "A(s) face(s) sorteada(s) foi(ram): $resultado "
        if(numeroDados == 2){
            resultado2 = geradorRandomico.nextInt(1..numeroFaces)
            nomeImagem2 = "dice_$resultado2"
            display += "e $resultado2"
            flagToShowDice = !(resultado2 > 6 || resultado > 6)
        }
        display.also { activityMainBinding.resultadoTv.text = it }

        if(flagToShowDice){
            activityMainBinding.resultadoIv.visibility = View.VISIBLE
            activityMainBinding.segundoIv.visibility = View.VISIBLE

            activityMainBinding.resultadoIv.setImageResource(
                resources.getIdentifier(nomeImagem, "mipmap", packageName)
            )
            activityMainBinding.segundoIv.setImageResource(
                resources.getIdentifier(nomeImagem2, "mipmap", packageName)
            )
        }
        else{
            activityMainBinding.resultadoIv.visibility = View.GONE
            activityMainBinding.segundoIv.visibility = View.GONE
        }
    }
}