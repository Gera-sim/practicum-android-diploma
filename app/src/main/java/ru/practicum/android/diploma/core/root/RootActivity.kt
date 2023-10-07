package ru.practicum.android.diploma.core.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
//import org.koin.android.BuildConfig
//import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chooseCountryFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.filtersFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.choosePlaceWorkFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.chooseRegionFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.filtersFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.chooseIndustry -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        // Пример использования access token для HeadHunter API
//        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}