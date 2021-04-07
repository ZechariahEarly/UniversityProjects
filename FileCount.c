#include<stdio.h>
int main()
{
   FILE *fp;
   int ch, count = 0;
   fp = fopen("infile.c","r");
   if(fp == NULL)
       printf("Failed to open.\n");
   else
   {
       printf("File open successful.\n");
       ch=fgetc(fp);
       while(ch!=EOF)
       {
           if(ch=='/')
           {
               ch=fgetc(fp);
               if(ch=='/')
               {
                   ch=fgetc(fp);
                   while(ch!='\n')
                   {
                       ch=fgetc(fp);
                       count++;
                   }
               }
               else if(ch=='*')
               {
                   ch=fgetc(fp);
                   while(ch!='\n' && ch!='*')
                   {
                       ch=fgetc(fp);
                       count++;
                   }
               }
           }
           else if(ch=='*')
           {
               ch=fgetc(fp);
               while(ch!='\n')
               {
                   ch=fgetc(fp);
               }
           }
           ch=fgetc(fp);
       }
       printf("Comment Count %d.\n",count);
   }
   return 0;
}
