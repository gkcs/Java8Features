import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
class Tein{
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;
    private SpaceCharFilter filter;
    public Tein(InputStream stream) {
        this.stream = stream;
    }
    public interface SpaceCharFilter {
        public boolean isSpaceChar(int ch);
    }
    public int read() {
        if (numChars == -1)
            throw new InputMismatchException();
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0)
                return -1;
        }
        return buf[curChar++];
    }
    public int readInt() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }
    public boolean isSpaceChar(int c) {
        if (filter != null)
            return filter.isSpaceChar(c);
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }
    public String readString() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuilder res = new StringBuilder();
        do {
            res.appendCodePoint(c);
            c = read();
        } while (!isSpaceChar(c));
        return res.toString();
    }
    public byte readByte() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        byte res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res;
    }
    public long readLong() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }
}
public class BichromeBoard
{
    public static void main(String[] args) throws IOException
    {
        Tein br=new Tein(System.in);
        StringBuilder sb=new StringBuilder();
        int n=br.readInt(),nt=n>>1,tn=n<<1,limit=((nt+7)>>3);
        int a[]=new int[n],ans=0;
        if(nt<21)
        {
            long diff=0;
            boolean shift=false,shift2=false,shift3=false;
            for(int i=0;i<nt;i++)
            {
                a[i]=br.readInt();
                diff=diff+a[i];
            }
            for(int i=nt;i<n;i++)
            {
                a[i]=br.readInt();
                diff=diff-a[i];
            }
            long mfit=diff;
            if(mfit<0)
            {
                mfit=-mfit;
            }
            int maximum=(1<<nt)-1;
            for(int i=1;i<maximum;i++)
            {
                long f=diff;
                int x=0,effort=0;
                boolean s=false,s2=false,s3=false;
                for(int j=0;j<nt;j++)
                {
                    if(((i>>j)&1)!=0)
                    {
                        f=f+((-a[nt-j-1]+a[nt+x])<<1);
                        x++;
                    }
                }
                for(int j=0;j<nt;j++)
                {
                    if(((i>>j)&1)!=0)
                    {
                        effort=effort+j+x+1;
                    }
                }
                long compare=m(f);
                if(effort+2<=tn&&m(f+((-a[nt+x-1]+a[nt+x])<<1))<compare)
                {
                    s=true;
                    compare=m(f+((-a[nt+x-1]+a[nt+x])<<1));
                }
                if(effort+4<=tn)
                {
                    if(m(f+((-a[nt+x-1]+a[nt+x+1])<<1))<compare)
                    {
                        s2=true;
                        compare=m(f+((-a[nt+x-1]+a[nt+x+1])<<1));
                    }
                    if(m(f+((-a[nt+x-2]+a[nt+x])<<1))<compare)
                    {
                        s3=true;
                        compare=m(f+((-a[nt+x-2]+a[nt+x])<<1));
                    }
                }
                if(effort<=tn&&f<mfit)
                {
                    mfit=compare;
                    ans=i;
                    shift=s;
                    shift2=s2;
                    shift3=s3;
                }
            }
            int pop=0;
            for(int i=0;i<nt;i++)
            {
                if(((ans>>i)&1)!=0)
                {
                    pop++;
                }
            }
            int q=0;
            for(int i=0;i<nt;i++)
            {
                if(((ans>>i)&1)!=0)
                {
                    sb.append(nt-i).append(' ').append(nt+pop-q).append('\n');
                    q++;
                }
            }
            if(shift3)
            {
                sb.append(nt-1).append(' ').append(nt).append('\n');
                sb.append(nt).append(' ').append(nt+1).append('\n');
                sb.insert(0,(pop+2)+"\n");
            }
            else if(shift2)
            {
                sb.append(nt+1).append(' ').append(nt+2).append('\n');
                sb.append(nt).append(' ').append(nt+1).append('\n');
                sb.insert(0,(pop+2)+"\n");
            }
            else if(shift)
            {
                sb.append(nt).append(' ').append(nt+1).append('\n');
                sb.insert(0,(pop+1)+"\n");
            }
            else
            {
                sb.insert(0,pop+"\n");
            }
        }
        else
        {
            byte gen[]=new byte[limit];
            long diff=0;
            int teffort=0;
            for(int i=0;i<nt;i++)
            {
                a[i]=br.readInt();
                diff=diff+a[i];
            }
            for(int i=nt;i<n;i++)
            {
                a[i]=br.readInt();
                diff=diff-a[i];
            }
            int pop=0;
            for(int i=0;i<(limit-1)&&(teffort+pop+2+(i<<3))<=tn;i++)
            {
                byte p[]=new byte[4];
                long fitness[]=new long[4];
                int effort[]=new int[4];
                for(int j=0;j<4;j++)
                {
                    p[j]=(byte)(Math.random()*255);
                    fitness[j]=diff;
                    effort[j]=teffort;
                    int x=0;
                    for(int l=0;l<8;l++)
                    {
                        if((p[j]&(1<<l))!=0)
                        {
                            effort[j]=effort[j]+l+x+2+(i<<3)+pop;
                            fitness[j]=fitness[j]+((-a[nt-l-1-(i<<3)]+a[nt+pop+x])<<1);
                            x++;
                        }
                    }
                }
                for(int j=0;j<250;j++)
                {
                    byte child[]=new byte[4];
                    double sf=0;
                    for(int k=0;k<4;k++)
                    {
                        sf=sf+1.0/(1+m(fitness[k]));
                        child[k]=p[k];
                    }
                    for(int k=0;k<4;k++)
                    {
                        double rand=Math.random();
                        int index=0;
                        while(rand>0)
                        {
                            rand=rand-1/(sf*(1+m(fitness[index])));
                            index++;
                        }
                        if(index!=0)
                        {
                            index--;
                        }
                        int index2=0;
                        rand=Math.random();
                        while(rand>0)
                        {
                            rand=rand-1/(sf*(1+m(fitness[index2])));
                            index2++;
                        }
                        if(index2!=0)
                        {
                            index2--;
                        }
                        p[k]=(byte)(child[index]&child[index2]&(15<<4));
                        p[k]=(byte)(p[k]|(child[index]|child[index2]&15));
                        if(Math.random()>0.98)
                        {
                            p[k]=(byte)(p[k]^(1<<(int)(Math.random()*8)));
                        }
                    }
                    for(int k=0;k<4;k++)
                    {
                        int x=0;
                        int oeffort=effort[k];
                        effort[k]=teffort;
                        for(int l=0;l<8;l++)
                        {
                            if((p[k]&(1<<l))!=0)
                            {
                                effort[k]=effort[k]+l+x+2+(i<<3)+pop;
                                x++;
                            }
                        }
                        int h=128;
                        while(effort[k]>tn)
                        {
                            x=0;
                            effort[k]=teffort;
                            p[k]=(byte)(p[k]&(h-1));
                            h=h>>1;
                            for(int l=0;l<8;l++)
                            {
                                if(((p[k]&(1<<l))!=0))
                                {
                                    effort[k]=effort[k]+l+x+2+(i<<3)+pop;
                                    x++;
                                }
                            }
                        }
                        long org=fitness[k];
                        fitness[k]=diff;
                        for(int l=0;l<8;l++)
                        {
                            if((p[k]&(1<<l))!=0)
                            {
                                x--;
                                fitness[k]=fitness[k]+((-a[nt-l-1-(i<<3)]+a[nt+pop+x])<<1);
                            }
                        }
                        if(m(fitness[k])>m(org))
                        {
                            p[k]=child[k];
                            fitness[k]=org;
                            effort[k]=oeffort;
                        }
                    }
                }
                for(int j=0;j<4;j++)
                {
                    if(m(fitness[j])<m(diff)&&effort[j]<=tn)
                    {
                        diff=fitness[j];
                        teffort=effort[j];
                        gen[i]=p[j];
                    }
                }
                for(int j=0;j<8;j++)
                {
                    if((gen[i]&(1<<j))!=0)
                    {
                        pop++;
                    }
                }
            }
            int q=0;
            for(int i=0;i<nt;i++)
            {
                if((gen[i>>3]&(1<<(i&7)))!=0)
                {
                    sb.append(nt-i).append(' ').append(nt+pop-q).append('\n');
                    q++;
                }
            }
            boolean shift=false,shift2=false,shift3=false;
            long compare=m(diff);
            if(teffort+2<=tn&&compare>m(diff+((-a[nt+pop-1]+a[nt+pop])<<1)))
            {
                compare=m(diff+((-a[nt+pop-1]+a[nt+pop])<<1));
                shift=true;
            }
            if(teffort+4<=tn)
            {
                if(compare>m(diff+((-a[nt+pop-1]+a[nt+pop+1])<<1)))
                {
                    compare=m(diff+((-a[nt+pop-1]+a[nt+pop+1])<<1));
                    shift2=true;
                }
                if(compare>m(diff+((-a[nt+pop-2]+a[nt+pop])<<1)))
                {
                    shift3=true;
                }
            }
            if(shift3)
            {
                sb.append(nt-1).append(' ').append(nt).append('\n');
                sb.append(nt).append(' ').append(nt+1).append('\n');
                sb.insert(0,(pop+2)+"\n");
            }
            else if(shift2)
            {
                sb.insert(0,(pop+2)+"\n");
                sb.append(nt+1).append(' ').append(nt+2).append('\n');
                sb.append(nt).append(' ').append(nt+1).append('\n');
            }
            else if(shift)
            {
                sb.insert(0,(pop+1)+"\n");
                sb.append(nt).append(' ').append(nt+1).append('\n');
            }
            else
            {
                sb.insert(0,pop+"\n");
            }
        }
        System.out.println(sb);
    }
    private static long m(long diff) {
        if(diff<0)
            return -diff;
        return diff;
    }
}