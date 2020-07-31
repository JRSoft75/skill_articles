package ru.skillbranch.skillarticles.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel

class ChoseCategoryDialog : DialogFragment() {
    private val viewModel: ArticlesViewModel by activityViewModels()
    private val selectedCategories: MutableList<String> = mutableListOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //TODO save checked state and  implement custom items
        val categories: Array<String> = args.categories.toList().map { "${it.title} (${it.articlesCount})" }.toTypedArray()
        val checked:BooleanArray = BooleanArray(args.categories.size){
            args.selectedCategories.contains(args.categories[it].categoryId)
        }
        val adb= AlertDialog.Builder(requireContext())
        .setTitle("Chose category")
            .setPositiveButton("Apply"){_,_ ->
                viewModel.applyCategories(selectedCategories)
            }
            .setNegativeButton("Reset"){_, _ ->
                viewModel.applyCategories(emptyList())
            }
            .setMultiChoiceItems(categories, checked) {dialog, wich, isChecked ->
                if(isChecked) selectedCategories.add(args.categories[wich].categoryId)
                else selectedCategories.remove(args.categories[wich].categoryId)
            }

        return adb.create()
    }




}